/* Show the declaration and use of function pointers.
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int
main(int argc, char *argv[]) {
	double (*F)(double), x=2.0;
	F = sqrt;
	printf("x=%.4f, F(x)=%.4f\n", x, F(x));
	F = sin;
	printf("x=%.4f, F(x)=%.4f\n", x, F(x));
	F = log;
	printf("x=%.4f, F(x)=%.4f\n", x, F(x));
	printf("x=%.4f, (*F)(x)=%.4f\n", x, (*F)(x));
	return 0;
}

/* =====================================================================
   Program written by Alistair Moffat, as an example for the book
   "Programming, Problem Solving, and Abstraction with C", Pearson
   Custom Books, Sydney, Australia, 2002; revised edition 2012,
   ISBN 9781486010974.

   See http://people.eng.unimelb.edu.au/ammoffat/ppsaa/ for further
   information.

   Prepared December 2012 for the Revised Edition.
   ================================================================== */
