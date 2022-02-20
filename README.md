# Moneydance Envelope Manager

Envelope Manager is an extension for [Moneydance](https://moneydance.com/) to help you use the Envelope Budgeting
System. It has been designed to work with Philip Stratton explanation of the system:

[![MoneyDance Tutorial #1 (fixed audio)](https://img.youtube.com/vi/eIcsMHL0NJ0/0.jpg)](https://www.youtube.com/watch?v=eIcsMHL0NJ0 "MoneyDance Tutorial #1 (fixed audio)")

## Features

* **Move money from envelopes to cover for uncleared expenses.** The extension will assist with the creation of
  transactions to represent the movement of money from an envelope to the main account. The auto-generated transaction
  will have the right values, and it'll also be linked to the original expense through metadata.

## Installation

1. Either [build the source code](#build) or [download the latest release](https://github.com/egrajeda/moneydance-envelope-manager/releases).

2. Follow [Moneydance's official documentation to install extensions](https://help.infinitekind.com/support/solutions/articles/80000682003-installing-extensions).

3. **The extension has not yet been audited and signed by The Infinite Kind**, so you'll get a confirmation box asking you if
   you really want to continue loading the extension, click on **Yes**.
   
4. Open the extension by going to **Extensions > Envelope Manager**.

## Build

1. Download the [Moneydance Developer's Kit](https://infinitekind.com/dev/moneydance-devkit-5.1.tar.gz) and extract it
   anywhere. Once extracted, copy-paste `lib/extadmin.jar` and `lib/moneydance-dev.jar` into the `lib/` directory:

```shell
cd tmp/
curl -O https://infinitekind.com/dev/moneydance-devkit-5.1.tar.gz
tar xzvf moneydance-devkit-5.1.tar.gz
cp moneydance-devkit-5.1/lib/* ... 
```

2. Download [Joda Money 1.0.1](https://www.joda.org/joda-money/) into the `lib/` directory.

```shell
curl -O https://github.com/JodaOrg/joda-money/releases/download/v1.0.1/joda-money-1.0.1.jar 
```

3. Generate a key pair (as required by Moneydance) to sign your locally built extension. The command needs to be 
   executed in `src/`:

```shell
ant genkeys
```

4. Build the extension from `src/`:

```shell
ant envelopemanager
```
