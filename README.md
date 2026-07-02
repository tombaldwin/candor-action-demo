# candor architecture-gate demo

A minimal, working example of the **[candor](https://github.com/tombaldwin/candor) PR-native
architecture gate** running in GitHub Actions on the JVM.

## What it shows

`src/main/java/com/demo/domain/Order.java` has a **domain** class whose `audit(…)` writes a file:

```java
public void audit(String msg) throws Exception {
    Files.write(Paths.get("/tmp/order-audit.log"), msg.getBytes());  // Fs — a boundary crossing
}
```

`arch.policy` forbids exactly that — the domain core must do no filesystem I/O:

```
deny Fs com.demo.domain
```

So the [`candor architecture gate` workflow](.github/workflows/candor.yml) **fails on purpose** (the red ✗
is the gate catching the deliberate violation), and — because this repo is public (code scanning is free on
public repos; private repos need GitHub Advanced Security) — the violation surfaces as a **code-scanning
alert on the exact line**, via SARIF:

> **AS-EFF-006** · `src/main/java/com/demo/domain/Order.java:11` · `Order.audit` performs `{ Fs }`,
> forbidden by policy: `deny Fs com.demo.domain`

See it under **Actions** (the workflow run) and **Security → Code scanning** (the alert). Delete the
`audit` method's body and the gate goes green.

## Wire it into your own JVM repo

Copy [`adopt/candor.yml`](https://github.com/tombaldwin/candor/blob/main/adopt/candor.yml) +
[`adopt/arch.policy`](https://github.com/tombaldwin/candor/blob/main/adopt/arch.policy) from the candor repo,
edit the package names, and you have an architecture gate on every push and PR. The gate runs
[candor-java](https://github.com/tombaldwin/candor-java) over your compiled bytecode — no annotations, no
source changes, any JVM language.
