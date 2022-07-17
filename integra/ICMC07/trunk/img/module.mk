local_dir := img
local_img := $(wildcard $(local_dir)/*.ps $(local_dir)/*.pdf $(local_dir)/*.png)

images += $(local_img)
