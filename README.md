# retroEMI
EMI is a featureful and accessible item and recipe viewer for Minecraft.

I accidentally ported it to 1.4.7 because NEI is a trash heap! Oopsie daisy!

## Port notes
To keep the ability to merge upstream changes, all upstream EMI code is in `xplat`, and (as much as
is feasible) shims that look like modern APIs are used rather than directly porting EMI code — many
classes are entirely unchanged from upstream, despite how different the underlying game is.

All code specific to this port, including those shims, is under the `nil` directory. The `forge` and
`fabric` directories are dead code. This is done with source sets, there's no Gradle subprojects.

[Jabel](https://github.com/bsideup/jabel) is used to permit usage of modern Java features while
compiling to Java 8, as 1.4 Forge won't run on anything newer.

Some ultra-fucky regexes in `build.gradle` are used to shim out newer Java APIs, like Stream.toList,
List.of, Set.of, Map.of, etc. I'm very sorry. Please be very careful if you decide to call a method
something like `toList` — prior versions of rEMI did this with manual refactors, but that made
upstream merges a *huge* chore.

## Why Nil instead of Forge/Voldeloom?
Because I felt like it. Also Nil has much more ergonomic patching, and NilGradle can use the
slightly-more-ergonomic [UnknownThingy](https://git.sleeping.town/Rewind/UnknownThingy) mappings
instead of legacy MCP.
