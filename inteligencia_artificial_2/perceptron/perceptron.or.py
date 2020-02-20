from random import choice
from numpy import array, dot, random
import matplotlib.pyplot as plt


unit_step = lambda x: 0 if x < 0 else 1

#datos de entrenamiento valores de entrada,bias, resultado esperado
#OR
training_data = [	(array([0,0,1]), 0), 
					(array([0,1,1]), 1), 
					(array([1,0,1]), 1), 
					(array([1,1,1]), 1), ]
w = random.rand(3)
errors = [] 
eta = 0.2
n = 100	
def xrange(n):
    return iter(range(n))

for i in xrange(n):
	x, expected = choice(training_data)
	result = dot(w, x)
	error = expected - unit_step(result)
	errors.append(error)
	w += eta * error * x
	
# # Run it for visualization of the progress
for x, _ in training_data:
	result = dot(x, w)
	print("{}: {} -> {}".format(x[:2], result, unit_step(result)))

plt.ylim([-1,1])
plt.plot(errors)
plt.xlabel('Errores')
plt.show()



