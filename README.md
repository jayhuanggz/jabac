Attribute based access control is an access control mechanism based on attributes of the request and the requested resource. For example, as a consumer you can only see orders that you have made an online shop, the shop owner can only see orders of his shops. In this case, in the ABAC domain context,  "**you"** are a **principal** in request for the "**order"** resource, and a **policy** is a rule saying that the request principal's "**id**" **attribute** must match the **"customer_id" attribute** of **order,** in order to perform the specified **action**

**jabac** is a java framework for developing ABAC protected systems. It is easy to use, highly extensible, and has support for integration with Spring security, Spring JPA, Hibernate and Mybatis.  Main features include:

- Expressive schema to define your policies
- Hibernate, Mybatis, JDBC, JPA integration. jabac provides annotations for your entity classes that map to resources defined in your policies, the framework will do the authorizations for your entities. 
- Spring security integration with built in filters and web authorizers. 

Modules in jabac:

- **jabac-core** - the core engine,  implemetation of abac authorizations, with dependency on Guava and mvel only. It is the only thing you need if you just need a simple "input -> output" abac engine.
- **jabac-client**- provides higher level abstractions and apis for interacting with the core engine. Annotations and processors to map entity classes to resources. Useful classes that help you develop abac facilities easier and in a better way.

jabac has the following main concepts:

- **Resource** - a protected resource, can be orders in your ecommerce system or a specific order, a file, a folder, etc.
- **Attributes** - key/value pairs of a resource or principal  
- **Policy** - an object that specifies evaluation rules
- **Principal** - an object that is requesting authorization, can be a loggedin user or a system
- **Condition** - defined inside a policy, a rule that the auth request must match for the policy to be applicable
- **Action** - an user defined action that can be performed on resources, can be "READ", "WRITE" , "DELETE", etc

Useage example:

```
Engine engine = new EngineBuilder().build();

Collection<Policy> policies = // create your policies

AuthContext context = new AuthContext(policies, 
new AuthRequestBuilder()
.principal(principal)
.resource(resource)
.action(action)
.build());

// evaluate access for the specific action requested by the principal on the  
// resource   
Effect effect = engine.evaluateAccess(context);

```





A policy example:



```

{
    "actions": [
        "order:*"
    ],
    "resources": [
        "service:order:*"
    ],
    "condition": {
        "equals": {
            "resource.attributes.tenantId": "principal.attributes.tenantId",
            "resource.attributes.merchantId": "principal.attributes.merchantId"
        },
        "in": {
            "resource.attributes.deparmtnId": "principal.attributes.deparmentIds"
        }
    }
}

```





The policy example means: for resources that starts with "service:order:",  actions that match "order:*" are allowed given the conditions are met:

1. tenantId of resource is equals to tenantId of principal
2. merchantId of resource is equals to merchantId of principal
3. departmentId of resource is one of the deparmentIds of principal

You can read examples in  **EngineTest**

