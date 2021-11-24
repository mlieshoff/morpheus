# morpheus
Morpheus is a small and simple code generation tool.

## Usage

On simplest you just start the class Morpheus with arguments in order

* path to model file
* path to generator property files directory
* path to templates directory 

### Model file
The XML model file must follow that schema "morpheus/src/main/resources/model.xsd".

### Generator property files

### Template files
Base of template processing is Velocity. You need to create <template_name>.vm files in order to have some generated 
output.

## Do you need more replacers?
Just create your custom implementation of the morpheus.Replacer interface and register it with a name you like to refer 
it in your templates.

