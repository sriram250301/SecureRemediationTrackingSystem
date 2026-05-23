# Secure Vulnerability Tracking Portal (Remediation Tracker)

A secure, role-based web application for creating, assigning, tracking, and auditing vulnerability findings.  
Built to demonstrate **secure web development practices** (authentication, authorization, validation, auditing, and secure error handling) in a realistic vulnerability management workflow.

- **Video demo (unlisted):** [PASTE_YOUTUBE_LINK_HERE]
- **Report:** Included in submission (NCI template)
- **Tech stack:** Spring Boot, Spring Security, Thymeleaf, Spring Data JPA, H2 (file-based)

---

## 1. Key Features

### Functional
- User authentication (login/logout)
- CRUD for vulnerability findings (create, view, update, delete)
- Assign vulnerabilities to developers
- Role-based dashboards/views
- Audit logging of security-relevant actions

### Roles & Permissions
| Role | Permissions (high level) |
|------|---------------------------|
| **ADMIN** | Manage users/roles, view all vulnerabilities, view audit logs |
| **ANALYST** | Create vulnerabilities, assign vulnerabilities, update vulnerability details |
| **DEVELOPER** | View assigned vulnerabilities, update remediation/status fields (as allowed) |

---

## 2. Security Objectives / Controls Implemented

- **Authentication & session management** using Spring Security
- **Password hashing** with **BCrypt**
- **RBAC**: route restrictions for `/admin/**`, `/analyst/**`, `/dev/**`, `/audit/**`
- **Object-level authorization** to prevent IDOR/horizontal privilege escalation
- **Server-side validation** using Jakarta Bean Validation (`@NotBlank`, `@Size`, etc.)
- **SQL injection mitigation** via parameterised ORM queries (Spring Data JPA)
- **XSS risk reduction** via Thymeleaf output encoding and validation constraints
- **Audit logging (DB-backed)** using an `AuditLog` entity/table
- **Secure error handling** via global exception handling (no stack traces to users)
 

## 3. Project Structure (typical)
> Actual package names may vary slightly.
