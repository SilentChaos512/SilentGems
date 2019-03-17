import io
import json

gems = [
    "ruby",
    "garnet",
    "topaz",
    "amber",
    "heliodor",
    "peridot",
    "green_sapphire",
    "phosphophyllite",
    "aquamarine",
    "sapphire",
    "tanzanite",
    "amethyst",
    "agate",
    "morganite",
    "onyx",
    "opal",
    "carnelian",
    "spinel",
    "citrine",
    "jasper",
    "zircon",
    "moldavite",
    "malachite",
    "turquoise",
    "euclase",
    "benitoite",
    "iolite",
    "alexandrite",
    "lepidolite",
    "ametrine",
    "black_diamond",
    "moonstone",
    "pyrope",
    "coral",
    "sunstone",
    "cats_eye",
    "yellow_diamond",
    "jade",
    "chrysoprase",
    "apatite",
    "fluorite",
    "kyanite",
    "sodalite",
    "ammolite",
    "kunzite",
    "rose_quartz",
    "tektite",
    "pearl",
]


def get_json(gem: str):
    return {
        'chaosGenerated': 3333,
        'processTime': 300,
        'ingredients': {
            'token': {
                'tag': 'forge:storage_blocks/' + gem
            },
            'others': [
                {
                    'item': 'silentgems:enriched_chaos_crystal',
                    'count': 10
                }
            ]
        },
        'result': {
            'item': 'silentgems:chaos_' + gem
        }
    }


if __name__ == '__main__':
    for gem in gems:
        data = get_json(gem)
        with open('output/' + gem + '.json', 'w', encoding='utf8') as f:
            json.dump(data, f, indent=2)
