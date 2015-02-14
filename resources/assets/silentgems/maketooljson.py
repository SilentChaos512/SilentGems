toolClasses = ["Sword", "Pickaxe", "Shovel", "Axe", "Hoe", "Sickle"]

def writeJSON(filename, tool, plus, toolClass):
  f = open(filename, 'w')
  f.write('{\n')
  f.write('  "parent": "builtin/generated",\n')
  f.write('  "textures": {\n')
  rodtype = toolClass + '_Rod'
  if plus:
    rodtype += 'Ornate'
    if toolClass == "Sword":
      f.write('    "layer0": "SilentGems:items/SwordDeco",\n')
    else:
      f.write('    "layer0": "SilentGems:items/ToolDeco",\n')
    f.write('    "layer1": "SilentGems:items/' + rodtype + '",\n')
    f.write('    "layer2": "SilentGems:items/' + tool + '"\n')
  else:
    rodtype += 'Normal'
    f.write('    "layer0": "SilentGems:items/' + rodtype + '",\n')
    f.write('    "layer1": "SilentGems:items/' + tool + '"\n')
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
  f.write('\n')


for tool in toolClasses:
  for i in range(12):
    name = tool + str(i)
    writeJSON(name + '.json', name, False, tool)
    writeJSON(name + 'Plus.json', name, True, tool)
