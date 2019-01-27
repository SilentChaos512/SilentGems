"""Generates blockstates and block/item model JSON files for gem blocks."""

import json
import io
import os

try:
    to_unicode = unicode
except NameError:
    to_unicode = str


gems = [
    'ruby',
    'garnet',
    'topaz',
    'amber',
    'heliodor',
    'peridot',
    'green_sapphire',
    'phosphophyllite',
    'aquamarine',
    'sapphire',
    'tanzanite',
    'amethyst',
    'agate',
    'morganite',
    'onyx',
    'opal',
    'carnelian',
    'spinel',
    'citrine',
    'jasper',
    'zircon',
    'moldavite',
    'malachite',
    'turquoise',
    'euclase',
    'benitoite',
    'iolite',
    'alexandrite',
    'lepidolite',
    'ametrine',
    'black_diamond',
    'moonstone',
    'pyrope',
    'coral',
    'sunstone',
    'cats_eye',
    'yellow_diamond',
    'jade',
    'chrysoprase',
    'apatite',
    'fluorite',
    'kyanite',
    'sodalite',
    'ammolite',
    'kunzite',
    'rose_quartz',
    'tektite',
    'pearl'
]


def write_json(path, data):
    file_path = 'output/' + path + '.json'
    if not os.path.exists(os.path.dirname(file_path)):
        os.makedirs(os.path.dirname(file_path))

    with io.open(file_path, 'w', encoding='utf8') as file:
        contents = json.dumps(data, sort_keys=True,
                              indent=2, separators=(',', ': '))
        file.write(to_unicode(contents))


def write_blockstate(file_path, model_path):
    write_json(file_path, {
        'variants': {
            '': {
                'model': 'silentgems:' + model_path
            }
        }
    })


def write_block_model(file_path, texture_path):
    write_json(file_path, {
        'parent': 'block/cube_all',
        'textures': {
            'all': 'silentgems:' + texture_path
        }
    })


def write_item_model(file_path, parent_path):
    write_json(file_path, {
        'parent': 'silentgems:' + parent_path
    })


# Details of all models to generate
blockTypes = {
    'block': {
        'names': [gem + '_block' for gem in gems],
        'textures': ['gem/' + gem + '_block' for gem in gems]
    },
    'bricks': {
        'names': [gem + '_bricks' for gem in gems],
        'textures': ['bricks/' + gem for gem in gems]
    },
    'glass': {
        'names': [gem + '_glass' for gem in gems],
        'textures': ['glass/' + gem for gem in gems]
    },
    'lamp': {
        'names': [gem + '_lamp' for gem in gems],
        'textures': ['lamp/' + gem for gem in gems]
    },
    'lamp_inverted': {
        'names': [gem + '_lamp_inverted' for gem in gems],
        'textures': ['lamp/' + gem for gem in gems]
    },
    'lamp_inverted_lit': {
        'names': [gem + '_lamp_inverted_lit' for gem in gems],
        'textures': ['lamp/' + gem + '_lit' for gem in gems]
    },
    'lamp_lit': {
        'names': [gem + '_lamp_lit' for gem in gems],
        'textures': ['lamp/' + gem + '_lit' for gem in gems]
    },
    'glowrose': {
        'names': [gem + '_glowrose' for gem in gems],
        'textures': ['glowrose/' + gem for gem in gems]
    },
    'ore': {
        'names': [gem + '_ore' for gem in gems],
        'textures': ['ore/gem/' + gem for gem in gems]
    }
}


for block_name, obj in blockTypes.items():
    for (name, texture) in zip(obj['names'], obj['textures']):
        print('Create JSON files for ' + name)
        print('        texture: ' + texture)
        write_blockstate('blockstates/' + name, 'block/' + name)
        write_block_model('models/block/' + name, 'blocks/' + texture)
        write_item_model('models/item/' + name, 'block/' + name)
