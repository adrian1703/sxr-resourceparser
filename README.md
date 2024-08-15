# Resource Parser for sxr

In order to construct a proper model for XRechnung I need schema-data 
in a machine readable format. Unfortunatly I was only able to 
acquire overview pdfs sofar. 

## Preliminary findings
I'm testing a couple libs in order to bring the PDF in a readable
format

### pypdf2
Simple text extraction - not too easy to work with but might get the 
job done with some quality cleanup.

### Spire.PDF
Does not do a good job at converting PDF to proper HTML. Not gonna 
investigate further.