#%% imports
from PyPDF2 import PdfReader
import pandas as pd
#%% read
path = "./../../resources/xrechnung-3.0.2-bundle-2024-06-20/XRechnung-v3.0.2.pdf"
reader = PdfReader(path)
pages = reader.pages[37:42]
pages = [page.extract_text() for page in pages]
pages = [page.replace("ADDRESSBG-12", "ADDRESS BG-12") for page in pages]
#%% modify pages
lines = "\n".join(pages).split("\n")
lines = [x for x in lines if "BT-" in x or "BG-" in x]
lines = [line.split(" ") for line in lines]
code_desc = [["code", "desc"]]
for tokens in lines:
    code = list(filter(lambda x: "BT-" in x or "BG-" in x, tokens))[0]
    code_index = tokens.index(code)
    desc = " ".join(tokens[:code_index])
    code_desc.append([code,desc])
#%% to dataframe
df = pd.DataFrame(code_desc[1:], columns=code_desc[0])
df['Prefix'] = df['code'].str.extract('(^[A-Z]{2}-)', expand=False)
df['Numeric'] = df['code'].str.extract('(\d+$)', expand=False).astype(int)
df = df.sort_values(by=['Prefix', 'Numeric'])
df.drop(['Prefix', 'Numeric'], axis=1, inplace=True)
print(df)
#%% save to csv
df.to_csv('output.csv', index=False)
