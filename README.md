LegoSorter - Axle and beamsorter
==========

Code for system sorting Lego axles and beams.

See http://www.youtube.com/watch?v=_kqBY0NkBYE for video and http://legohackers.blogspot.com/ for blog.

Requirements for code
==========
 - leJOS SDK
 - IntelliJ IDEA
 
 
Requirements for running
==========
You will need
 - At least 3 NXT's with leJOS (0.9.1) installed
 - Bluetooth or USB connection to them
 - Give NXT's names according to submodules (Sorter, Loader, Distributor, Feeder)
 - Pair with the NXT's (computer -> NXT)
 - Pair the Loader with Sorter and Distributor (it will start bluetooth communication with them)

When you want to upload the program for a submodule, simply execute the run-configuration with the same name as the submodule. This will execute the uploadandrun ant target for that submodule. 
