f = open("/Users/Alejo/Desktop/UNC/4 - Primer Semestre/Programación Concurrente/Tp_Final/tpMonitor/tinvariantes.txt","r")
transInvariants0 = f.read().split("\n")
transInvariants1 = []
for i in transInvariants0:
    transInvariants1.append(i.split(" "))
f.close()

f = open("/Users/Alejo/Desktop/UNC/4 - Primer Semestre/Programación Concurrente/Tp_Final/tpMonitor/disparos.txt","r")
log = f.read().split(" ")
f.close()

print("Invariantes: ",transInvariants1)
print(log)

counter = 0
index = []
invCounter = 0

for i in range(200):
    for i in range(len(transInvariants1)):
        for j in range(len(transInvariants1[i])):
            for k in range(len(log)):
                if(transInvariants1[i][j] == log[k]):
                    index.append(k)
                    counter = counter+1
                    break
        if(counter == len(transInvariants1[i])):
            index.reverse()
            for i in index:
                #print("Len:",len(log))
                log.pop(i)
            index.clear()
            counter = 0
            invCounter = invCounter+1
        else:
            index.clear()
            counter = 0

print(log)
print("Cantidad de invariantes encontrados: ",invCounter)