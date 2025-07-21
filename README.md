# Programming Language ANalysis by Examples (PLANE)

[![GitHub License](https://img.shields.io/github/license/0x1DOCD00D/PLANE)](LICENSE)

## Overview

Programming Language ANalysis by Examples (PLANE) is a research‑oriented Scala/Java project that explores automated, example‑driven techniques for understanding and comparing programming‑language semantics. It gives researchers and practitioners tooling to prototype analyses quickly, extract semantic insights, and study cross‑language behaviour from concrete code snippets.

## Goals

* Provide a repeatable workflow for example‑based language analysis.
* Automate extraction of semantic properties from curated program fragments.
* Support systematic, side‑by‑side evaluation of multiple languages and runtimes.
* Remain easily extensible so new analyses and front‑ends can be plugged in with minimal friction.

## Key Features

* **Example Repository** – A declarative JSON/YAML format for encoding code samples and expected observations.
* **Static & Dynamic Analyses** – Leverages ASM, Spoon, Scala Meta and custom passes to combine static inspection with runtime tracing.
* **Cross‑Language Harness** – Uniform test driver that can compile/interpret many languages and normalise their outputs for comparison.
* **Plugin Architecture** – Add new analyses by dropping a module in `src/main/scala/…​` and declaring it in `build.sbt`.

## Installation

Clone the repository:

```bash
git clone https://github.com/0x1DOCD00D/PLANE.git
cd PLANE
```

### Prerequisites

| Tool       | Recommended version                  |
| ---------- |--------------------------------------|
| **JDK**    | 21 (with `--enable‑preview` support) |
| **sbt**    | 1.11.2 (or later)                    |
| **Python** | 3.10+                               |

### Getting the Scala/Java dependencies

`sbt` takes care of Scala and Java libraries declared in **build.sbt**:

```bash
# fetch and update all jars
sbt update
```

### Getting the Python helpers (optional)

Some helper scripts live in `scripts/` and require a small Python stack:

```bash
pip install -r requirements.txt
```

## Usage

The project is organised as a single sbt module.

Compile everything and run the default driver:

```bash
sbt run
```

Run the test‑suite:

```bash
sbt test
```

### Analysing a custom example set

```bash
sbt "run --examples examples/integer‑coercions.yaml --language scala"
```

The CLI flags are documented in `--help`.

## Project Structure

```
PLANE/
├── src/
│   ├── main/
│   │   ├── scala/        # Core analysis engine, plugins and CLI driver
│   │   └── resources/    # Reference grammars, templates, config
│   └── test/
│       ├── scala/        # Unit and property tests
│       └── resources/    # Test fixtures and sample programs
├── project/               # sbt meta‑build (plugins, build.properties)
├── build.sbt              # Build definition, library dependencies
├── scripts/               # Auxiliary Python helpers (tokenisers, plotting)
├── docs/                  # Architecture notes and usage guides (optional GitHub Pages)
├── .scalafmt.conf         # Formatting rules
├── .sbtopts               # JVM flags used by sbt (preview features, memory)
├── .gitignore
└── README.md
```

`target/` and `.idea/` are generated directories created by sbt and IntelliJ IDEA respectively and are therefore omitted from version control.

## Contributing

1. Fork the repository and create a feature branch.
2. Follow the ScalaStyle Guide; run `sbt scalafmtAll` before committing.
3. Add or update tests in `src/test/scala`.
4. Open a pull request and briefly describe the rationale behind the change.

## License

PLANE is released under the Apache License – see [LICENSE](LICENSE) for details.

## Contact

* GitHub issues are the preferred channel for bug reports and feature requests.
* For research collaboration, reach out to [@0x1DOCD00D](https://github.com/0x1DOCD00D).

## Acknowledgements

This project draws inspiration from the PLDI’20 “Language Design by Example” vision and the Spoon/ASM ecosystems. Thanks to all contributors who keep PLANE flying high!
