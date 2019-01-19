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


BLOCK_TYPE = 'gem'

for gem in gems:
    block = BLOCK_TYPE + '/' + gem
    print('Create JSON files for ' + block)
    write_blockstate('blockstates/' + block, 'block/' + block)
    write_block_model('models/block/' + block, 'blocks/' + block)
    write_item_model('models/item/' + block, 'block/' + block)
