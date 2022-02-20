# Moneydance Envelope Manager

Envelope Manager is an extension for [Moneydance](https://moneydance.com/) to help you use the Envelope Budgeting
System. It has been designed to work with Philip Stratton explanation of the system:

[![MoneyDance Tutorial #1 (fixed audio)](https://img.youtube.com/vi/eIcsMHL0NJ0/0.jpg)](https://www.youtube.com/watch?v=eIcsMHL0NJ0 "MoneyDance Tutorial #1 (fixed audio)")

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