import re

#open text file in read mode
file = open("./disparos.txt", "r")
data = file.read()

regex = r"(T0,)(.*?)((T1,)(.*?)(T3,)(.*?)((T12,)(.*?)(T6,)(.*?)((T8,)(.*?)(T14,)(.*?)|(T9,)(.*?)(T15,)(.*?))|(T16,)(.*?)((T8,)(.*?)(T14,)(.*?)|(T9,)(.*?)(T15,)(.*?)))|(T2,)(.*?)(T4,)(.*?)((T13,)(.*?)(T7,)(.*?)((T10,)(.*?)(T14,)(.*?)|(T11,)(.*?)(T15,)(.*?))|(T5,)(.*?)((T10,)(.*?)(T14,)(.*?)|(T11,)(.*?)(T15,)(.*?))))"
groups ='\g<2>\g<5>\g<7>\g<10>\g<12>\g<15>\g<17>\g<19>\g<21>\g<23>\g<26>\g<28>\g<30>\g<32>\g<34>\g<36>\g<39>\g<41>\g<44>\g<46>\g<48>\g<50>\g<52>\g<55>\g<57>\g<59>\g<61>'

matches = 1

while(matches != 0):
    data, matches = re.subn(regex, groups, data)
    print("data = ", data)
    print("matches = ", matches)
