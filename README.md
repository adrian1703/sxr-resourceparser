# Modelgeneration for SXR

The general idea is to parse Peppol Invoice Syntax model to 
generate Protobuf Types. From there it should be possible to generate
Frontend aswell as Backend Types.

## Prerequisites

### Install Protobuf compiler
https://github.com/protocolbuffers/protobuf/releases

For Windows: Download + add to path

Verify with ```protoc -h```

## Usage
1. Generate proto-model using the **modelgen** Modul(Groovy).
