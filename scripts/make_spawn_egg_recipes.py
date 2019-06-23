import io
import json

types = [
    'bat',
    'blaze',
    'cave_spider',
    'chicken',
    'cod',
    'cow',
    'creeper',
    'dolphin',
    'donkey',
    'drowned',
    'elder_guardian',
    'enderman',
    'endermite',
    'evoker',
    'ghast',
    'guardian',
    'horse',
    'husk',
    'llama',
    'magma_cube',
    'mooshroom',
    'mule',
    'ocelot',
    'parrot',
    'phantom',
    'pig',
    'polar_bear',
    'pufferfish',
    'rabbit',
    'salmon',
    'sheep',
    'shulker',
    'silverfish',
    'skeleton',
    'skeleton_horse',
    'slime',
    'spider',
    'squid',
    'stray',
    'tropical_fish',
    'turtle',
    'vex',
    'villager',
    'vindicator',
    'witch',
    'wither_skeleton',
    'wolf',
    'zombie',
    'zombie_horse',
    'zombie_pigman',
    'zombie_villager',
]


def get_json(entity_type: str):
    return {
        'type': 'silentgems:token_enchanting',
        'chaosGenerated': 1000,
        'processTime': 200,
        'ingredients': {
            'token': {
                'item': 'minecraft:egg'
            },
            'others': [
                {
                    'type': 'silentgems:soul_gem',
                    'soul': 'minecraft:' + entity_type,
                    'count': 4
                },
                {
                    'item': 'silentgems:enriched_chaos_crystal',
                    'count': 4
                }
            ]
        },
        'result': {
            'item': 'minecraft:' + entity_type + '_spawn_egg'
        }
    }


if __name__ == '__main__':
    for entity_type in types:
        data = get_json(entity_type)
        with open('output/' + entity_type + '.json', 'w', encoding='utf8') as f:
            json.dump(data, f, indent=2)
