import sys, os, re

ARMOR_CLASSES = ("Helmet", "Chestplate", "Leggings", "Boots")
TYPE_COUNT = 12
#EXTRA_TYPES = ("Fish", "Flint", "Chaos")

def createDirIfNeeded(name):
    if not os.path.exists(name):
        os.makedirs(name)

def createAllDirs():
    createDirIfNeeded('output/blockstates')
    createDirIfNeeded('output/models/block')
    createDirIfNeeded('output/models/item')

def writeJSON(armor, id, plus, armorClass):
    armor_name = armor + str(i) + ('Plus' if plus else '')
    filename = 'output/models/item/' + armor_name + '.json'
    print("Writing", filename)
    f = open(filename, 'w')
    f.write('{\n')
    f.write('  "parent": "builtin/generated",\n')
    f.write('  "textures": {\n')
    
    if plus:
        f.write('    "layer0": "SilentGems:items/' + armor + str(i) + '",\n')
        f.write('    "layer1": "SilentGems:items/' + armor + 'Extra"\n')
    else:
        f.write('    "layer0": "SilentGems:items/' + armor + str(i) + '"\n')

    f.write('  },\n')
    f.write('  "display": {\n')
    f.write('    "thirdperson": {\n')
    f.write('      "rotation": [-90, 0, 0],\n')
    f.write('      "translation": [0, 1, -3],\n')
    f.write('      "scale": [0.55, 0.55, 0.55 ]\n')
    f.write('    },\n')
    f.write('    "firstperson": {\n')
    f.write('      "rotation": [0, -135, 25 ],\n')
    f.write('      "translation": [0, 4, 2 ],\n')
    f.write('      "scale": [1.7, 1.7, 1.7]\n')
    f.write('    }\n')
    f.write('  }\n')
    f.write('}\n')
    #f.write('\n')

createAllDirs()

for armor in ARMOR_CLASSES:
    for i in range(TYPE_COUNT):
        name = armor
        writeJSON(name, i, False, armor)
        writeJSON(name, i, True, armor)
    #for type in EXTRA_TYPES:
    #    name = armor + type
    #    writeJSON(name + '.json', name, False, armor)
#