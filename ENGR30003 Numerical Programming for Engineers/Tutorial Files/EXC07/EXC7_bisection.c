/* The bisection method for finding a root of an equation.
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define NITER 1000

typedef struct {
    double x;
    double fx;
    double eps;
    int iter;
} root_t;


double f(double);
root_t bisection(double,double,double,int);

int main(int argc, char *argv[]) {
	root_t root;
	double LEFT,RGHT;
    double eps = 1e-4;
    
	printf("\nSpecify lower value (x_l): ");
    scanf("%lf",&LEFT);
	
	printf("\nSpecify upper value (x_u): ");
    scanf("%lf",&RGHT);
        
    root = bisection(LEFT, RGHT, eps, NITER);
    printf("x = %.12E, f(x) = %.12E eps=%.1E iterations=%d\n",root.x,root.fx,root.eps,root.iter);
    
	return 0;
}


double f(double x) {
    return pow(x,4) + 30*exp(-x/4)-60;
}

root_t bisection(double xl, double xu, double eps, int limit) {

    double fxl, fxu, xr, fxr = 1;
    int iterations=0;
	fxl = f(xl);
	fxu = f(xu);
    
	while (fabs(fxr) >= eps && iterations < limit) {

        iterations = iterations+1;

        xr  = (xl+xu)/2.0;
		fxr = f(xr);
        
		if (fxl*fxr < 0) {/* root is to left of middle */
			xu  = xr;
			fxu = fxr;
		} else { /* root is to right */
			xl  = xr;
			fxl = fxr;
		}
	}
    
    root_t root;
    root.x  = xr;
    root.fx = fxr;
    root.eps = eps;
    root.iter = iterations;
	return root;
}

/* =====================================================================
   Aman Kidanemariam 11.09.20
   =================== */

