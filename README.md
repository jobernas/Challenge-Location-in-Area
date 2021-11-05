# Testing-Location-in-Area
Testing if My location is in a given area.

Scenery:    
 As FAIRTIQ user,
 I want to be informed when I leave a region supported by FAIRTIQ,
 so that I can act and find myself a valid ticket to continue my journey.

 In short: detect if a device is outside of a given area and inform observers.
 Implementation details:
     - To avoid outliers and computing too much, at least 2 filters have to be applied.
     - At least 3 positions have to be detected outside of a given area before notifying observers.
     - After a notification to observers, notifications are paused for 5 minutes.
     - Computing this only makes sense when a journey is ongoing.

 Email to: sl@fairtiq.com