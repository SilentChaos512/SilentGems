"""Create entries for gem blocks/items for localization (lang) files.

Translators: You could optionally use this for your translations:
    - In the gems dict: change the values (right of the colon ':')
    - In blocks and items, edit the first part of the value as needed (gem + ' Block Name' or whatever)
    - Do NOT edit block_key or item key
    - Do NOT edit the for loops at the bottom

Suggested use: direct output to a file:
    py make_loc_keys.py > loc_output.txt
Note this output is NOT a complete valid lang file. It just prints the individual lines.
"""

gems = {
    'ruby': 'Ruby',
    'garnet': 'Garnet',
    'topaz': 'Topaz',
    'amber': 'Amber',
    'heliodor': 'Heliodor',
    'peridot': 'Peridot',
    'green_sapphire': 'Green Sapphire',
    'phosphophyllite': 'Phosphophyllite',
    'aquamarine': 'Aquamarine',
    'sapphire': 'Sapphire',
    'tanzanite': 'Tanzanite',
    'amethyst': 'Amethyst',
    'agate': 'Agate',
    'morganite': 'Morganite',
    'onyx': 'Onyx',
    'opal': 'Opal',
    'carnelian': 'Carnelian',
    'spinel': 'Spinel',
    'citrine': 'Citrine',
    'jasper': 'Jasper',
    'zircon': 'Zircon',
    'moldavite': 'Moldavite',
    'malachite': 'Malachite',
    'turquoise': 'Turquoise',
    'euclase': 'Euclase',
    'benitoite': 'Benitoite',
    'iolite': 'Iolite',
    'alexandrite': 'Alexandrite',
    'lepidolite': 'Lepidolite',
    'ametrine': 'Ametrine',
    'black_diamond': 'Black Diamond',
    'moonstone': 'Moonstone',
    'pyrope': 'Pyrope',
    'coral': 'Coral',
    'sunstone': 'Sunstone',
    'cats_eye': "Cat's Eye",
    'yellow_diamond': 'Yellow Diamond',
    'jade': 'Jade',
    'chrysoprase': 'Chrysoprase',
    'apatite': 'Apatite',
    'fluorite': 'Fluorite',
    'kyanite': 'Kyanite',
    'sodalite': 'Sodalite',
    'ammolite': 'Ammolite',
    'kunzite': 'Kunzite',
    'rose_quartz': 'Rose Quartz',
    'tektite': 'Tektite',
    'pearl': 'Pearl'
}


def block_key(block):
    return 'block.silentgems.' + block


def item_key(item):
    return 'item.silentgems.' + item


blocks = {
    'ore': {
        'keys': [block_key(gem + '_ore') for gem in gems.keys()],
        'values': [gem + ' Ore' for gem in gems.values()]
    },
    'block': {
        'keys': [block_key(gem + '_block') for gem in gems.keys()],
        'values': ['Block of ' + gem for gem in gems.values()]
    },
    'bricks': {
        'keys': [block_key(gem + '_bricks') for gem in gems.keys()],
        'values': [gem + ' Bricks' for gem in gems.values()]
    },
    'glass': {
        'keys': [block_key(gem + '_glass') for gem in gems.keys()],
        'values': [gem + ' Glass' for gem in gems.values()]
    },
    'lamp': {
        'keys': [block_key(gem + '_lamp') for gem in gems.keys()],
        'values': [gem + ' Lamp' for gem in gems.values()]
    },
    'lamp_inverted': {
        'keys': [block_key(gem + '_lamp_inverted') for gem in gems.keys()],
        'values': ['Inverted ' + gem + ' Lamp' for gem in gems.values()]
    },
    'lamp_inverted_lit': {
        'keys': [block_key(gem + '_lamp_inverted_lit') for gem in gems.keys()],
        'values': ['Inverted ' + gem + ' Lamp' for gem in gems.values()]
    },
    'lamp_lit': {
        'keys': [block_key(gem + '_lamp_lit') for gem in gems.keys()],
        'values': [gem + ' Lamp' for gem in gems.values()]
    },
    'glowrose': {
        'keys': [block_key(gem + '_glowrose') for gem in gems.keys()],
        'values': [gem + ' Glowrose' for gem in gems.values()]
    }
}

items = {
    'gem': {
        'keys': [item_key(gem) for gem in gems.keys()],
        'values': [gem for gem in gems.values()]
    },
    'shard': {
        'keys': [item_key(gem + '_shard') for gem in gems.keys()],
        'values': [gem + ' Shard' for gem in gems.values()]
    }
}

others = {
    'sgear_mats': {
        'keys': ['material.silentgems.main_' + gem for gem in gems.keys()],
        'values': [gem for gem in gems.values()]
    }
}


for _, obj in blocks.items():
    for (key, value) in zip(obj['keys'], obj['values']):
        print('  "' + key + '": "' + value + '",')

for _, obj in items.items():
    for (key, value) in zip(obj['keys'], obj['values']):
        print('  "' + key + '": "' + value + '",')

for _, obj in others.items():
    for (key, value) in zip(obj['keys'], obj['values']):
        print('  "' + key + '": "' + value + '",')
