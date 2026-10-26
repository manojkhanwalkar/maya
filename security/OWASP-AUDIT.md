# OWASP Top 10 (2025) Audit — maya

_Date: 2026-10-26 · Scope: whole repository · Commit: 911497b · Tree: clean_

## System summary
maya is a set of Dropwizard 2.0 (Jersey/Jetty) payment-security prototype services, each with its own `main` and YAML config: **vault** (tokenizes card numbers with FPE or salted SHA3 hashes, and returns the PAN for a token), **issuer** (issues cards with PAN, expiry date and CVV), **ccvid** (card verification: calls the PM scorer and VFS velocity service), **otp** (sends OTPs by AWS SES/SNS and validates them), **pm** (stub risk score) and **vfs** (velocity counters kept in memory and in day files on disk). Vault, issuer and ccvid take requests as JWE(ECDH-ES/A256GCM)-wrapped JWS(ES256) payloads. The keys are agreed through an unauthenticated `/keyexchange`, where any caller registers its own JWK set. OTP, PM and VFS take plain JSON. Key assets: PANs, CVVs, OTPs, FPE keys and the HTTPS keystore. There is no README. Severity is rated as if the services were deployed as written, because they handle real-format card data.

## Summary
| Category | Status | Findings |
|---|---|---|
| A01 Broken Access Control | Issues | C:1 H:0 M:1 L:0 |
| A02 Security Misconfiguration | Issues | C:0 H:1 M:1 L:0 |
| A03 Software Supply Chain Failures | Issues | C:0 H:1 M:0 L:0 |
| A04 Cryptographic Failures | Issues | C:0 H:3 M:0 L:1 |
| A05 Injection | Issues | C:0 H:0 M:1 L:0 |
| A06 Insecure Design | Issues | C:0 H:1 M:2 L:0 |
| A07 Authentication Failures | Issues | C:0 H:0 M:1 L:0 |
| A08 Software or Data Integrity Failures | Issues | C:1 H:0 M:0 L:0 |
| A09 Security Logging and Alerting Failures | Issues | C:0 H:0 M:1 L:1 I:1 |
| A10 Mishandling of Exceptional Conditions | Issues | C:0 H:0 M:0 L:1 |

Total: 19 findings: Critical 2, High 6, Medium 7, Low 3, Info 1.

The two most serious problems both come from how the services treat callers. First, Jackson polymorphic default typing was enabled on every payload the services parse, which opens the door to deserialization gadget chains. Second, `/keyexchange` lets anyone register keys, so the encrypted and signed channel authenticates no one: any caller can detokenize any merchant's card numbers from the vault. Fix these first, in this order: (1) remove default typing (done), (2) add a real client trust model, such as pre-registered client keys or mutual TLS, and bind merchant IDs to the authenticated client (deferred, needs a decision), (3) use a CSPRNG for OTPs, PANs and CVVs and put limits on OTP attempts and sends (done).

## Remediation
| Finding | Severity | Status | Notes |
|---|---|---|---|
| A08-1 Jackson default typing on remote input | Critical | Fixed | `enableDefaultTyping()` removed; regression test |
| A01-1 Unauthenticated key exchange → anyone can detokenize | Critical | Deferred (needs decision) | trust-model change; see options |
| A04-1 OTP / PAN / CVV from ThreadLocalRandom | High | Fixed | SecureRandom |
| A02-1 TLS hostname verification disabled JVM-wide | High | Fixed (operator impact) | opt-in env var for local dev |
| A04-2 Keystore password hardcoded in 6 configs | High | Fixed (operator impact) | env vars; rotate |
| A06-1 OTP brute force and SMS/email pumping | High | Partially fixed | attempt and send limits; endpoint still unauthenticated |
| A03-1 Known-vulnerable dependencies | High | Fixed | in-major bumps, unused libraries removed |
| A04-3 PANs stored in clear (VFS files, vault memory) | High | Deferred (needs decision) | data-format / KMS design |
| A02-2 Admin connectors on all interfaces | Medium | Fixed (operator impact) | `bindHost: 127.0.0.1` |
| A06-2 Unbounded key registration and key-id takeover | Medium | Fixed | cap, size limit, reject reused key IDs |
| A06-3 `/nonce` count unbounded | Medium | Fixed | 1..100 per request, store capped |
| A05-1 VFS line injection, unknown attributes, racy map | Medium | Fixed | allowlist, control chars rejected, ConcurrentHashMap |
| A09-1 PAN / OTP / headers written to stdout | Medium | Fixed (server paths) | sample client mains still print |
| A07-1 No replay protection on signed requests | Medium | Deferred (needs decision) | protocol change |
| A01-2 PM and VFS services unauthenticated | Medium | Deferred (needs decision) | deployment / network decision |
| A10-1 Token-type fall-through; errors become empty 204 responses | Low | Partially fixed | fall-through and PAN validation fixed |
| A04-4 OTP compared with `String.equals` | Low | Fixed | `MessageDigest.isEqual` |
| A09-2 No security event logging (`printStackTrace` only) | Low | Not fixed (recommendation) | |
| A09-3 Personal email and phone number in sample mains | Info | Not fixed | |

**Verification:**
- Toolchain: Maven 3.9.9 (`~/.m2/wrapper/dists/apache-maven-3.9.9/3477a4f1/bin/mvn`) on JDK 25. The only JDK 21 installed has no `javac`. Building required Lombok 1.18.16 → 1.18.48 and an explicit `annotationProcessorPaths` entry, because JDK 23+ no longer runs annotation processors it finds on the classpath.
- Baseline (before any fixes): `mvn test` passed but ran 0 tests. The existing test classes are manual mains.
- After the fixes: `mvn test` ran 13 new regression tests, all passing (`util.SecurityFixesTest`, `vault.TokenManagerTest`, `otp.OTPManagerLimitsTest`, `ccvid.NonceManagerTest`). The default-typing test fails when `JSONUtil.java` is reverted to the old version, so it does catch a regression.
- Live checks (local self-signed keystore with CN/SAN=localhost):
  - Starting `VaultApplication` without `MAYA_KEYSTORE_*` stops with `UndefinedEnvironmentVariableException`.
  - With the variables set, `ss -ltnp` shows the application port on `*:9180` and the admin port on `127.0.0.1:9181`.
  - `vault.VaultClient` round-trips FPE, FPE1 and Hash tokens end to end through key exchange, JWE and JWS. This also shows that Jackson without default typing and nimbus-jose-jwt 9 still interoperate.
  - Connecting to `https://127.0.0.1:9180/` with a certificate issued for `localhost` now fails with `SSLHandshakeException: No subject alternative names matching IP address 127.0.0.1`. Hostname verification is enforced.

**Breaking changes for operators:**
- Every service now needs `MAYA_KEYSTORE_PATH` and `MAYA_KEYSTORE_PASSWORD` in its environment, and will not start without them. The old absolute keystore path is no longer in the YAML.
- Admin ports (8181, 9281, 8281, 9181, 8881) are bound to 127.0.0.1 only.
- Outbound HTTPS from `util.Connection` now verifies hostnames. Certificates must match the host name used (`localhost`). For local development only, `MAYA_TLS_DISABLE_HOSTNAME_VERIFICATION=true` restores the old behaviour.
- JSON payloads no longer carry or accept Jackson type IDs (`["class", {...}]`). All senders in this repository use the same `JSONUtil`, so they stay compatible. An external client that relied on type IDs would need to change.
- `/otp`: at most 3 sends per recipient per minute (HTTP 429 after that), and a 400 if `type` or `phoneNumOrEmail` is missing. `/verify` invalidates an OTP after 5 wrong guesses.
- `/nonce`: `number` must be 1..100.
- `/keyexchange`: returns an empty response for key sets with more than 20 keys, for reused key IDs, or once 50,000 key IDs are registered.
- VFS ignores attributes other than `ccv` and `ip`, and values longer than 64 characters or containing control characters.
- Dependencies: Dropwizard 2.0.10 → 2.0.35, Jackson 2.10 → 2.12.7.1, nimbus-jose-jwt 8.19 → 9.37.3, Guava 19 → 32.1.3, commons-io 2.6 → 2.16.1, commons-codec 1.11 → 1.17.1, BouncyCastle `jdk15on` 1.64 → `jdk18on` 1.78.1, SolrJ 8.6.0 → 8.11.4, JUnit 4.12 → 4.13.2, Lombok 1.18.16 → 1.18.48. The full `aws-java-sdk` 1.11.327 is replaced by `aws-java-sdk-ses`/`-sns` 1.12.780 (and `-s3` in test scope). Removed as unused: `tink`, `jcl-core`, AWS SDK v2 (`bom` was declared as a dependency, plus `aws-sdk-java` and `dynamodb-enhanced`).

**Follow-ups:**
- Rotate the keystore password. The old value stays in git history and in every clone. The keystore it protects (`mytestkey.jks`) lives outside this repository.
- Decide a client trust model for A01-1, A07-1 and A01-2.
- Decide PAN-at-rest protection for A04-3.
- Consider authenticating `/otp` and moving rate limits to the gateway.
- Remove the personal email address and phone number from the sample mains.

## Findings

### A01 — Broken Access Control

#### [Critical] Unauthenticated key exchange makes the signed channel anonymous: any caller can detokenize any merchant's PANs (Confirmed)
**Status:** Deferred (needs decision). This finding asks for a trust model, which changes the protocol. Partial hardening went in under A06-2 (key-ID takeover blocked, registrations capped). The live check reproduced the problem: an anonymous `VaultClient` registered keys and got PANs back from `/cc`.
- **Location:** `src/main/java/util/KeyExchangeUtil.java:10-30`, `src/main/java/vault/VaultResource.java:44-86`, `src/main/java/vault/VaultGetRequestProcessor.java:31`, and the same pattern in `issuerbank/IssuerResource.java` and `ccvid/CCVIDResource.java`
- **Evidence:**
  ```java
  ClientHandler clientHandler = new ClientHandler();
  clientHandler.init(request.getJavawebKeySet());      // caller-supplied keys, no identity check
  ...
  Optional<String> pan = tokenManager.get(request.getMerchantId(), request.getToken()); // merchantId from body
  ```
- **Scenario:** An attacker POSTs its own JWK set to `/keyexchange`, signs `{"merchantId":"Merchant1","token":{...}}` with its own key and calls `/cc`, which returns the clear PAN. Tokens are format-preserving (the last 4 digits are kept), so they leak easily. The same flow lets anyone call `/issue` to mint cards with CVVs, or `/tokenize` against any merchant.
- **Fix options:**
  - (a) Accept only pre-registered client signing keys (an allowlist in config or a key registry), and bind each key to a merchant ID that the server sets instead of reading it from the body.
  - (b) Require mutual TLS and map client certificates to merchants.
  - (c) Put an authenticating API gateway in front and pass the merchant identity in a trusted header.

#### [Medium] PM and VFS services are unauthenticated and listen on all interfaces (Plausible)
**Status:** Deferred (needs decision). Binding them to localhost would break any multi-host deployment.
- **Location:** `src/main/java/pm/PMResource.java:39-74`, `src/main/java/vfs/service/VFSResource.java:38-48`, `src/main/resources/pm.yml`, `vfs.yml`
- **Scenario:** Anyone who can reach port 8880 can inflate or skew the velocity counters for any card or IP, which manipulates fraud decisions. Port 8280 serves risk scores to anyone. This is exploitable only if those ports are reachable beyond the ccvid host.
- **Fix:** Bind both services to localhost or a private network, or require mTLS or a shared service credential from ccvid.

### A02 — Security Misconfiguration

#### [High] TLS hostname verification disabled for the whole JVM (Confirmed)
**Status:** Fixed. Verification is on by default. `MAYA_TLS_DISABLE_HOSTNAME_VERIFICATION=true` is an explicit opt-out for local development. Checked live: a request to 127.0.0.1 with a certificate issued for `localhost` is now rejected.
- **Location:** `src/main/java/util/Connection.java:27`
- **Evidence:** `props.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");`
- **Scenario:** This runs inside ccvid, which calls PM and VFS. Any party with a certificate trusted by the JVM for *any* name can man-in-the-middle those calls and read card numbers or forge scores.
- **Fix:** Leave hostname verification on, and issue certificates for the real host names.

#### [Medium] Admin connectors exposed on all interfaces (Confirmed)
**Status:** Fixed. All six configs set `bindHost: 127.0.0.1`. Checked live with `ss -ltnp`.
- **Location:** `src/main/resources/*.yml` (`adminConnectors`)
- **Scenario:** The Dropwizard admin port serves metrics, thread dumps and tasks (log-level, GC) without authentication. Anyone who can reach it gets information disclosure and some operational control.
- **Fix:** Bind the admin port to localhost or a management network.

### A03 — Software Supply Chain Failures

#### [High] Known-vulnerable dependency versions (Confirmed, by version)
**Status:** Fixed. All bumps stay within the same major version, except two cases:
- nimbus-jose-jwt 8→9. Two call sites changed from `toJSONObject().toJSONString()` to `toString()`.
- The BouncyCastle artifact was renamed from `jdk15on` to `jdk18on`.

The Dropwizard 2.0.35 BOM is imported so transitive libraries line up. The Jackson 2.12.7.1 BOM is imported ahead of it. Verified with `mvn test` and the live vault round-trip.
- **Location:** `pom.xml`
- **Evidence (before):**
  - Dropwizard 2.0.10, which brings Jetty 9.4.29, Jackson 2.10.4 and Logback 1.2.3.
  - Explicit Jackson modules at 2.10.0.
  - nimbus-jose-jwt 8.19 with json-smart 2.3: JWE/JWS/JWK parsing of attacker input, CVE-2021-27568, CVE-2023-1370, CVE-2023-52428.
  - Guava 19.0 (CVE-2018-10237, CVE-2020-8908, CVE-2023-2976) and commons-io 2.6 (CVE-2021-29425).
  - bcprov 1.64, SolrJ 8.6.0 (pulls ZooKeeper 3.5.7 and Netty 4.1.47), and the entire AWS SDK 1.11.327.
- **Scenario:** Several of these parse attacker-controlled input: JSON bodies, JOSE headers and JWK sets. Known denial-of-service and deserialization CVEs apply.
- **Fix:** Done as above. Not done: SnakeYAML 1.26 stays as Dropwizard 2.0.x pins it. CVE-2022-1471 needs attacker-controlled YAML, and only the operator's config files are parsed. Upgrading past it means Dropwizard 2.1+ or 4.x, which is a framework major change.

### A04 — Cryptographic Failures

#### [High] OTPs, PANs and CVVs generated with ThreadLocalRandom shared through a static field (Confirmed)
**Status:** Fixed. `SMSUtil` and `CardUtil` now use `SecureRandom`. Covered by tests.
- **Location:** `src/main/java/otp/sms/SMSUtil.java:7`, `src/main/java/util/CardUtil.java:23,317`
- **Evidence:** `static ThreadLocalRandom random = ThreadLocalRandom.current();`
- **Scenario:** ThreadLocalRandom is not cryptographically secure, so its output can be predicted. Captured in a static field, a single generator is shared by every request thread, which ThreadLocalRandom does not support. Predictable OTPs allow account takeover. Predictable CVVs and PANs make issued cards guessable.
- **Fix:** `SecureRandom`.

#### [High] Keystore password hardcoded in every service config (Confirmed)
**Status:** Fixed. Configs use `${MAYA_KEYSTORE_PATH}` and `${MAYA_KEYSTORE_PASSWORD}`, resolved by a strict `EnvironmentVariableSubstitutor` in each `*Application.initialize`. Checked live: startup fails when the variables are unset. **The old password is in git history: rotate it.**
- **Location:** `src/main/resources/ccvid.yml, issuer.yml, otp.yml, pm.yml, vault.yml, vfs.yml` (`keyStorePassword: "****"`, 8 characters, a dictionary word)
- **Scenario:** Anyone with the repository or the built jar (the configs sit in `src/main/resources`) gets the TLS private-key password.
- **Fix:** Done.

#### [High] PANs stored in clear: VFS day files on disk, and in vault memory with the FPE key next to each token (Confirmed)
**Status:** Deferred (needs decision). Protecting these values changes the on-disk format and the vault's key management.
- **Location:**
  - `src/main/java/ccvid/CCVIDRequestProcessor.java:68` (`transactionData.put("ccv", cardNumber)`) → `vfs/CurrentFileManager.java:102-108`, which writes each value as a line in `/home/manoj/data/vfs/current/ccv_<date>`.
  - `src/main/java/util/CardUtil.java:200,253` (the FPE key and tweak are stored in the same tuple as the token).
  - `CardUtil.java:312` (Hash tokens keep the PAN).
- **Scenario:** Any read access to the VFS data directory or a vault heap dump yields full card numbers. The vault design offers no separation between keys and data.
- **Fix options:**
  - VFS: key the counters by a keyed hash (HMAC-SHA256 with a secret key) of the PAN rather than the PAN itself.
  - Vault: encrypt the PAN under a KMS-held key, and keep FPE keys per merchant in a KMS rather than per token in the same map.

#### [Low] OTP compared with `String.equals` (Confirmed)
**Status:** Fixed. Now uses `MessageDigest.isEqual`.
- **Location:** `src/main/java/otp/OTPManager.java:84`

### A05 — Injection

#### [Medium] VFS writes caller-controlled values as lines in its data files; unknown attributes and racy maps (Confirmed)
**Status:** Fixed.
- Only the `ccv` and `ip` attributes are accepted.
- Values must be 1–64 characters with no control characters.
- `currentCountMap` is now a `ConcurrentHashMap`.
- **Location:** `src/main/java/vfs/VFSManager.java:81-96`, `vfs/CurrentFileManager.java:102`, `vfs/VelocityStatsManager.java:19,43`
- **Scenario:** A value containing `\n` injects extra records into the day file, which are counted at recovery and skew velocity checks. An unknown attribute triggers an NPE. Concurrent `computeIfAbsent` on a `HashMap` can corrupt the map.

### A06 — Insecure Design

#### [High] OTP: unlimited guesses and unlimited sends on unauthenticated endpoints (Confirmed)
**Status:** Partially fixed.
- An OTP is invalidated after 5 wrong guesses.
- Sends are limited to 3 per recipient per minute (HTTP 429), and input is validated.
- Expired OTPs are now also removed from the lookup map. Before, they were only removed from the queue and accumulated forever.
- `ExpiredOTPProcessor` is synchronized.

Authenticating callers of `/otp` is deferred because it needs the A01-1 trust model. Covered by tests.
- **Location:** `src/main/java/otp/OTPManager.java:70-102`, `otp/OTPResource.java`, `otp/ExpiredOTPProcessor.java:26-37`
- **Scenario:** Anyone can make the service send SMS or email to arbitrary numbers and addresses (toll fraud, harassment), and can retry guesses on an OTP until it expires.

#### [Medium] Unbounded key registration and key-ID takeover in `/keyexchange` (Confirmed)
**Status:** Fixed. Key sets must have 1–20 keys. A key exchange is rejected if any key ID is already registered (`put` used to overwrite). Registrations are capped at 50,000 key IDs. Covered by tests.
- **Location:** `src/main/java/util/ClientCache.java:37`, `util/KeyExchangeUtil.java`
- **Scenario:** Each call generates 20 EC key pairs and stores them forever, which exhausts CPU and memory. A caller that reuses a victim's key ID redirects the victim's traffic to the attacker's handler.

#### [Medium] `/nonce` generates and stores an attacker-chosen number of nonces (Confirmed)
**Status:** Fixed. `number` must be 1..100, and the store is capped at 100,000. Covered by tests.
- **Location:** `src/main/java/ccvid/NonceManager.java:12-28`
- **Scenario:** `{"number": 2147483647}` triggers an out-of-memory error on the ccvid service.

### A07 — Authentication Failures

#### [Medium] No replay protection or freshness on signed requests (Confirmed)
**Status:** Deferred (needs decision). The JWS payloads carry no timestamp, nonce or audience, and the server never checks or consumes the nonces from `/nonce`.
- **Location:** `src/main/java/util/ClientHandler.java:84-94`, `util/JWUtil.java:94` (`//TODO - set and process expiry time`)
- **Scenario:** A captured `/tokenize`, `/issue` or `/verify` message can be replayed indefinitely.
- **Fix:** Add `iat`/`exp` and a one-time nonce (consumed from `NonceManager`) to the signed payload, and reject stale or reused ones.

### A08 — Software or Data Integrity Failures

#### [Critical] Jackson polymorphic default typing applied to remote payloads (Confirmed)
**Status:** Fixed. `enableDefaultTyping()` is removed. A regression test shows a `["java.util.concurrent.atomic.AtomicInteger",5]` value now deserializes as a plain list. The test fails against the old code. The live vault round-trip still works.
- **Location:** `src/main/java/util/JSONUtil.java:16`
- **Evidence:** `mapper.enableDefaultTyping();` The mapper is used for every request body in vault, issuer and ccvid after unwrapping. `CCVIdRequest` and `Attributes` have `@JsonAnySetter ... Object value`.
- **Scenario:** Combined with A01-1, any caller can send `"x": ["<gadget class>", {...}]` and Jackson instantiates that class. With Jackson 2.10.4 and the large classpath (AWS SDK, Solr, Logback), this is a known path to remote code execution.
- **Fix:** Done. Never enable default typing on untrusted input.

### A09 — Security Logging and Alerting Failures

#### [Medium] Card numbers, OTPs and headers printed to stdout (Confirmed)
**Status:** Fixed on server paths. The prints were removed from `VFSClient.submit` (whose result echoes the card number) and `PMClient.submit`. Also removed: the `Connection.post` response print, the JWE header and verify prints in `JWUtil`, and the OTP value from `IdOTPTimeTuple.toString` and `ExpiredOTPProcessor`. `CMResource` no longer prints the `ClickResponse` header. Not changed: the manual client mains (`VaultClient`, `Sender.UnwrapResponse`, `client/*`) still print payloads to the developer's console.
- **Location:** `src/main/java/vfs/VFSClient.java:47`, `otp/ExpiredOTPProcessor.java:32`, `util/Connection.java:67`, `util/JWUtil.java:63,167`
- **Scenario:** PANs and live OTPs end up in container and service logs, which breaks PCI DSS requirements for PAN handling.

#### [Low] No security event logging (Confirmed)
**Status:** Not fixed (recommendation). Failures (bad signature, unknown key ID, OTP mismatches, rate-limit hits) are either swallowed or dumped with `printStackTrace` to stdout. There is no logger and no alerting.
- **Fix:** Log these events through SLF4J at WARN, without secrets, and alert on spikes.

#### [Info] Personal email address and phone number hardcoded in sample mains
- **Location:** `src/main/java/otp/sms/EmailSMS.java:35,47`, `otp/sms/PhoneSMS.java:45`
- **Fix:** Read them from configuration.

### A10 — Mishandling of Exceptional Conditions

#### [Low] Token-type fall-through in the vault; failures returned as empty 204 responses (Confirmed)
**Status:** Partially fixed.
- `TokenManager.get` now returns per case and checks the stored tuple's type. FPE tuples were also mislabelled `FPE1` and Hash tuples had no type; both are corrected.
- `validateCard` rejects anything other than 12–19 digits. Before, bad input threw `NumberFormatException` or `ArrayIndexOutOfBoundsException`, and a short PAN could make `FPEencrypt1` fail.

Not changed: `ClientManager.process` and the processors still return `null` (HTTP 204) on every error. Adding proper error responses changes the wire contract. Covered by tests.
- **Location:** `src/main/java/vault/TokenManager.java:72-120`, `util/ClientManager.java:39-45`, `util/CardUtil.java:124`
- **Scenario:** An FPE1 token lookup fell through into the FPE and Hash branches and ran the wrong decryption or casts. Errors are indistinguishable from success with an empty body, and leave no log trail.

## Out of scope / not checked
- No CVE scanner was run. OWASP dependency-check, osv-scanner, trivy and gitleaks are not installed. A03 is based on resolved versions from `mvn dependency:list` and known CVEs.
- Git history: no keystore, key or `.env` files have ever been committed (`git log --all -- '*.jks' '*.p12' '*.pem' '*.key' '.env'` is empty). The keystore password was committed (see A04-2).
- Runtime deployment (network exposure of each port, the gateway, and how the keystore at `/home/manoj/IdeaProjects/securitytest/...` was provisioned) could not be observed.
- Offline utilities (`batch/*`, `util/GVAPCHFUtil/*`, `client/*`, `nimbustest/CertTest`, the Solr helpers) are developer tools with no network entry point. They were reviewed only for secrets and dangerous sinks, and none were found.
- The `com.idealista.fpe` FF1 implementation copied into `src/main/java/com/idealista` was not reviewed for cryptographic correctness.
