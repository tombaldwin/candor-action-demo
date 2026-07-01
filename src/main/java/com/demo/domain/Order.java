package com.demo.domain;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Order {
    public int total(int a, int b) { return a + b; }          // pure — fine

    // A domain object writing a file: exactly the boundary the policy forbids.
    public void audit(String msg) throws Exception {
        Files.write(Paths.get("/tmp/order-audit.log"), msg.getBytes());
    }
}
