# Jackson deserialisation bug reproduction

Under a certain set of conditions, Jackson will throw the following exception during deserialistion:
```
com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Conflicting setter definitions for property "defaultPropertyInclusion": com.fasterxml.jackson.databind.ObjectMapper#setDefaultPropertyInclusion(com.fasterxml.jackson.annotation.JsonInclude$Include) vs com.fasterxml.jackson.databind.ObjectMapper#setDefaultPropertyInclusion(com.fasterxml.jackson.annotation.JsonInclude$Value)
```

We observed this happening when:
- injecting a reference via `@JacksonInject`
- when that reference is a `@Bean`
- and that reference autowires (either directly or indirectly) a `@Bean` reference to an `ObjectMapper`
- and that `ObjectMapper` has the Kotlin module installed
