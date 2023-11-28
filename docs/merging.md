## Merging

Developers can merge two resource-packs together using the
`ResourcePack#merge(ResourcePack, MergeStrategy)` method.

<!--@formatter:off-->
```java
// get our resource packs
ResourcePack base = ...;
ResourcePack other = ...;

// merge
base.merge(other, MergeStrategy.override());

// now base contains all the resources from other
```
<!--@formatter:on-->

There are three built-in merge strategies:

- `override()`: overrides the resources of the base resource-pack with the resources
  of the second one if there are duplicates.
- `mergeAndFailOnError()`: merges the resources of the base resource-pack with the
  resources of the second one, resulting in a `MergeException` if there are duplicates that
  can't be merged.
- `mergeAndKeepFirstOnError()`: merges the resources of the
  base resource-pack with the resources of the second one,
  keeping only the resources of the first resource-pack if there
  are duplicates that can't be merged.