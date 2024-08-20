# Modelgeneration for SXR

The general idea is to parse Peppol Invoice Syntax model to 
generate Protobuf Types. From there it should be possible to generate
Frontend aswell as Backend Types.

## Prerequisites

### Install Protobuf compiler
https://github.com/protocolbuffers/protobuf/releases

For Windows: Download + add to path

Verify with 
```bash 
protoc -h
```

## Usage
1. Generate proto-model using the **modelgen** Modul(Groovy).
2. Generate language specific code.
```bash
 protoc --kotlin_out=modelgen/kotlin_out --java_out=modelgen/kotlin_out --proto_path=modelgen/proto-model DELIVERY_INFORMATION.proto
```