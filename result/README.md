# MAX Error Cause

Looking through the datasets, the maximal error is given when we have just a few requests. 
Since we just ask the file choosing randomly from two servers without any load balancing tecnique, all the requests are taken by the other server and one of them is free.
This happened and so we have an hour with burst of full link usage and just one measurement where the link is free. This is the problem, because for all the time the switch is loaded at its maximum, except for that measurements and so it expects to have the haviest load instead of the smallest one.