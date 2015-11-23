import sys, os, re

TOOL_CLASSES = ("Sword", "Pickaxe", "Shovel", "Axe", "Hoe", "Sickle", "Bow")
HEAD_COUNT = 15
TIP_COUNT = 3
DECO_COUNT = 15
WOOL_COUNT = 16

def createDirIfNeeded(name):
    if not os.path.exists(name):
        os.makedirs(name)

def createAllDirs():
    createDirIfNeeded('output/blockstates')
    createDirIfNeeded('output/models/block')
    createDirIfNeeded('output/models/item')

def writeJSON(part_name, scale):
    filename = part_name + '.json'
    print("Writing", filename)
    f = open('output/models/item/' + filename, 'w')
    
    scale_third_person = tuple(0.85*x for x in scale)
    scale_first_person = tuple(1.7*x for x in scale)

    f.write('{\n')
    f.write('  "parent": "builtin/generated",\n')
    f.write('  "textures": {\n')
    f.write('    "layer0": "SilentGems:items/' + part_name + '"\n')
    f.write('  },\n')
    f.write('  "display": {\n')
    f.write('    "thirdperson": {\n')
    f.write('      "rotation": [0, 90, -35],\n')
    f.write('      "translation": [0, 1.25, -3.5],\n')
    scale_string = ', '.join(str(scale_third_person[x]) for x in range(3))
    f.write('      "scale": [' + scale_string + ']\n')
    f.write('    },\n')
    f.write('    "firstperson": {\n')
    f.write('      "rotation": [0, -135, 25 ],\n')
    f.write('      "translation": [0, 4, 2 ],\n')
    scale_string = ', '.join(str(scale_first_person[x]) for x in range(3))
    f.write('      "scale": [' + scale_string + ']\n')
    f.write('    }\n')
    f.write('  }\n')
    f.write('}\n')
    #f.write('\n')

createAllDirs()

scale_deco = 3 * (1.01,)
scale_wool = 3 * (1.03,)
scale_rod = 3 * (1.00,)
scale_head_inner = 3 * (1.01,)
scale_head_outer = 3 * (1.04,)
scale_tip = 3 * (1.06,)

# Shared objects
# Blank and Error
writeJSON('Blank', 3 * (1,))
writeJSON('Error', 3 * (1.1,))
# Deco
for i in range(DECO_COUNT):
    writeJSON('ToolDeco' + str(i), scale_deco)
# Wool
for i in range(WOOL_COUNT):
    writeJSON('RodWool' + str(i), scale_wool)

# Tool specific objects
for tool in TOOL_CLASSES:
    # Heads
    for i in range(HEAD_COUNT):
        name = tool + str(i)
        writeJSON(name, scale_head_inner)
        writeJSON(name + 'L', scale_head_outer)
        writeJSON(name + 'R', scale_head_outer)
        if tool == 'Bow':
            writeJSON(name + '_3', scale_head_inner)
            writeJSON(name + 'L_3', scale_head_outer)
            writeJSON(name + 'R_3', scale_head_outer)

    # Tips
    for i in range(TIP_COUNT):
        name = tool + 'Tip' + str(i)
        writeJSON(name, scale_tip)
        if tool == 'Bow':
            writeJSON(name + '_3', scale_tip)
        

    # Deco
    if tool == 'Sword' or tool == 'Bow':
        for i in range(DECO_COUNT):
            writeJSON(tool + 'Deco' + str(i), scale_deco)

    # Wool
    if tool == 'Sword' or tool == 'Sickle':
        for i in range(WOOL_COUNT):
            writeJSON(tool + 'Wool' + str(i), scale_wool)

    # Rods
    if tool == 'Bow':
        for i in range(4):
            writeJSON(tool + '_MainNormal' + str(i), scale_rod)
            writeJSON(tool + '_MainOrnate' + str(i), scale_rod)
    else:
        writeJSON(tool + '_RodNormal', scale_rod)
        writeJSON(tool + '_RodOrnate', scale_rod)

    # Bow Arrows
    if tool == 'Bow':
        for i in range(1, 4):
            writeJSON(tool + '_Arrow' + str(i), 3 * (1.02,))