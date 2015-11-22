import sys, os, re

TOOL_CLASSES = ("Sword", "Pickaxe", "Shovel", "Axe", "Hoe", "Sickle", "Bow")
TYPE_COUNT = 12
EXTRA_TYPES = ("Fish", "Flint", "Chaos")


def createDirIfNeeded(name):
    if not os.path.exists(name):
        os.makedirs(name)

def createAllDirs():
    createDirIfNeeded('output/blockstates')
    createDirIfNeeded('output/models/block')
    createDirIfNeeded('output/models/item')

def writeJSON(filename, tool, plus, toolClass):
    print("Writing", filename)
    f = open('output/models/item/' + filename, 'w')

    f.write('{\n')
    f.write('  "parent": "builtin/generated",\n')
    f.write('  "textures": {\n')

    rod_type = ''
    if (toolClass == 'Bow'):
        rod_type = toolClass + '_Main' + ('Ornate0' if plus else 'Normal0')
    else:
        rod_type = toolClass + '_Rod' + ('Ornate' if plus else 'Normal')
    deco_type = 'SwordDeco12' if toolClass == 'Sword' else 'ToolDeco12'
    tool_texture = tool.replace('Fish', '12').replace('Flint', '13').replace('Chaos', '14')

    if plus:
        f.write('    "layer0": "SilentGems:items/' + rod_type + '",\n')
        if toolClass != 'Bow':
            f.write('    "layer1": "SilentGems:items/' + tool_texture + '",\n')
            f.write('    "layer2": "SilentGems:items/' + deco_type + '"\n')
        else:
            f.write('    "layer1": "SilentGems:items/' + tool_texture + '"\n') # no comma
    else:
        f.write('    "layer0": "SilentGems:items/' + rod_type + '",\n')
        f.write('    "layer1": "SilentGems:items/' + tool_texture + '"\n') # no comma

    f.write('  },\n')
    f.write('  "display": {\n')
    f.write('    "thirdperson": {\n')
    f.write('      "rotation": [0, 90, -35],\n')
    f.write('      "translation": [0, 1.25, -3.5],\n')
    f.write('      "scale": [0.85, 0.85, 0.85 ]\n')
    f.write('    },\n')
    f.write('    "firstperson": {\n')
    f.write('      "rotation": [0, -135, 25 ],\n')
    f.write('      "translation": [0, 4, 2 ],\n')
    f.write('      "scale": [1.7, 1.7, 1.7]\n')
    f.write('    }\n')
    f.write('  }\n')
    f.write('}\n')
    f.write('\n')

createAllDirs()

for tool in TOOL_CLASSES:
    for i in range(TYPE_COUNT):
        name = tool + str(i)
        writeJSON(name + '.json', name, False, tool)
        writeJSON(name + 'Plus.json', name, True, tool)
    for type in EXTRA_TYPES:
        name = tool + type
        writeJSON(name + '.json', name, type == 'Chaos', tool)
