# ellipse-perimeter

Calculating the perimeter of an ellipse takes an infinite series. There have been many approximations proposed over the years to ease computation.
Inspired by the videos below of Matt Parker and Three feet of Air, I have devised my own solution.

First I attempted a crude approximation in Java which I've included for my own records. I then drafted a python version using scipy.optimize.curve_fit in Python 3.

The resulting equation is:
![equation](https://latex.codecogs.com/svg.latex?P%20%3D%20a%20%5Cleft%20%28%20%5Cleft%20%28%202%20%5Cpi%20-4%20%5Cright%20%29%20%5Cleft%20%28%5Cfrac%7Bb%7D%7Ba%7D%20%5Cright%20%29%5EE%20&plus;%204%20%5Cright%20%29)
Where E is the constant found using least squares curve fitting:
![equation](https://latex.codecogs.com/svg.latex?E%20%3D%201.458131%20%5Cpm%20.000012)

I have a couple ideas as to what this constant could be, though I fear hardware limitations and floating-point accuracy could hinder my insights.
The observed value of E is close to the following values, though I cannot prove it is equivalent to either:
![equation](https://latex.codecogs.com/svg.latex?%5Cfrac%7B%5Cpi%7D%7B%5Cpi%20-%201%7D) ![equation](https://latex.codecogs.com/svg.latex?%5Csqrt%20%5B3%5D%20%7B%5Cpi%7D)

[Maclaurin Series, see Infinite Series 1](https://www.mathsisfun.com/geometry/ellipse-perimeter.html#:~:text=When%20a%3Db%2C%20the%20ellipse,..%20in%20our%20example\).)
[Matt Parker Video](https://www.youtube.com/watch?v=5nW3nJhBHL0&ab_channel=Stand-upMaths)
[Three Feet of Air Video](https://www.youtube.com/watch?v=qXTGVNwOz0w)
[Wikipedia article on Ellipses](https://en.wikipedia.org/wiki/Ellipse)
