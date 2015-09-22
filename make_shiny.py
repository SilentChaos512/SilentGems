# A script for GIMP used to generate textures for items and such with gem subtypes.
# This is not the original, that was lost (because I kept it on my desktop for some reason).
# So, this isn't quite look the script that generated the tool textures, but close enough.

hue = [   0,  10,  20,  50,  80, 100, 180, -140, -100, -80, -50,    0 ]
lit = [   0,   0,   0,   0, -20, -10, -15,   20,    0,  -5,  80, -100 ]
sat = [   0,   0,   0,   0,   0,   0, -10,    0,    0,   0,   0, -100 ]
path = "D:\\abyss\\output\\"
name = "GemArmor"
number = ""

g = gimp.pdb
images = gimp.image_list()
my_image = images[0]
width = g.gimp_image_width(my_image)
height = g.gimp_image_height(my_image)
layer = g.gimp_image_get_layer_by_name(my_image, "default")

for i in range(0, 12):
  new_layer = layer.copy(True)
  new_layer.name = "gem" + str(i)
  my_image.add_layer(new_layer)
  g.gimp_hue_saturation(new_layer, 0, hue[i], lit[i], sat[i])
  filename = name + str(i) + ".png"
  g.file_png_save(my_image, new_layer, path + filename, filename, 0, 0, 0, 0, 0, 0, 0)