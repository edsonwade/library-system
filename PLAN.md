# Project Plan

## Sprint 1: Setup & Initial Implementation

- **Duration:** 2 weeks
- **Tasks:**
    - Project setup
    - Database design
    - Initial entities and relationships
    - Basic CRUD operations for books

## Sprint 2: Member Management & Book Issuing

- **Duration:** 2 weeks
- **Tasks:**
    - Implement member management
    - Issue and return books
    - Basic validations

## Sprint 3: Fine Management & Reporting

- **Duration:** 2 weeks
- **Tasks:**
    - Implement fine calculation
    - Report generation
    - Admin functionalities

## Sprint 4: Search & Final Integrations

- **Duration:** 2 weeks
- **Tasks:**
    - Implement search functionality
    - Complete integrations
    - Perform testing

## Sprint 5: Testing & Deployment

- **Duration:** 1 week
- **Tasks:**
    - Final testing
    - Bug fixes
    - Deployment preparation

- @Disabled("This test is disabled because the update operation is not yet implemented.")

- **Working with Bigdecimal**: instead of double and float, because Bigdecimal is precise compare to double and float.

```java
public class Account {

    private BigDecimal balance;

    // your code goes here

    public Account() {
        this.balance = new BigDecimal("0.0");
    }

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);

    }

    public String getBalanceString() {
        return String.valueOf(this.balance);
    }

}
```

What will be the output when the following code is executed?

```java
    public class Example {

    private static Double number;

    public static void main(String[] args) {
        if (number == 0.0) {
            System.out.println("The number is 0");
        }
    }

}
R: NullPointerException
```
Good job!
The boxed primitive Double number will have the initial value of null, so the check (number == 0.0) will throw an exception.


1. What does Instant.now() return?
```Instant.now() always returns the time in the timezone```.
