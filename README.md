CurveArea calculator - Akka
===========================

Area Calculator calculates area that is a region which lies under a given curve f(x) between 2 points on x-axis (between x<sub>1</sub> and x<sub>2</sub>).

The sum is called a Riemann sum and can be described:

![\sum_{k=1}^{Int.Max}f\left(x_k\right) \Delta x](http://www.sciweavers.org/tex2img.php?eq=%5Csum_%7Bk%3D1%7D%5E%7BInt.Max%7Df%5Cleft%28x_k%5Cright%29%20%5CDelta%20x&bc=White&fc=Black&im=jpg&fs=12&ff=arev&edit=0)

width: &Delta;x =  (x<sub>2</sub> - x<sub>1</sub>) / Int.Max

right endpoint: x<sub>k</sub> = x<sub>1</sub> + k&Delta;x

height: f(x<sub>k</sub>)


Purpose
-------

To obtain maximum accuracy of area, width (x<sub>2</sub> - x<sub>1</sub>) is partitioned by Int.Max. For each segment
 calculation f(x) is performed and summed.

As calculation of f(x) for Int.Max segments is not trivial and we use akka to make calculation concurrently using 8 akka workers.

To compare performance, we can calculate the same area without akka.

Usage
-----

```
sbt
> run

Multiple main classes detected, select one to run:

 [1] com.rizn.akka.CurveArea
 [2] com.rizn.akka.CurveAreaAkka
```

Performance
-----------

To calculate area of function -<sup>1</sup>&frasl;<sub>10</sub> x<sup>2</sup> + 2x + 2 between x<sub>1</sub>=3 and x<sub>2</sub>=10
it took (on the same machine):

 no akka (CurveArea): 24090 milliseconds
 
 akka (CurveAreaAkka): 2875 milliseconds
 
 
On average, calculation with akka is 10x faster.