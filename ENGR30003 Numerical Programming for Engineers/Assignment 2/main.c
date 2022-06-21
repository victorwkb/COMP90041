/***************************************************************************
 *
 *   File        : main.c
 *   Student Id  : 981436
 *   Name        : Kai Bing Goh
 *
 ***************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <sys/time.h>
#include <string.h>
#include "tasks.h"

int main(int argc, char *argv[])
{
	/* TODO: Parse Command Line Arguments
	DONOT explicitly set arguments to filenames */
	char *q1_file = argv[1];
	char *q3_file = argv[2];
	char *q4_file = argv[3];
	double xo = atof(argv[4]);
	char *q5_file = argv[5];

	/* TODO: Add timing for each task and output running time in ms */
	struct timeval start;
	struct timeval stop;

	/* Question 1 */
	gettimeofday(&start, NULL);
	shockwave(q1_file);
	gettimeofday(&stop, NULL);
	double elapsed_ms = (stop.tv_sec - start.tv_sec) * 1000.0;
	elapsed_ms += (stop.tv_usec - start.tv_usec) / 1000.0;
	printf("QUESTION 1:  %.2f milliseconds\n", elapsed_ms);

	/* Question 3 */
	gettimeofday(&start, NULL);
	linsolve(q3_file);
	gettimeofday(&stop, NULL);
	elapsed_ms = (stop.tv_sec - start.tv_sec) * 1000.0;
	elapsed_ms += (stop.tv_usec - start.tv_usec) / 1000.0;
	printf("QUESTION 3:  %.2f milliseconds\n", elapsed_ms);

	/* Question 4 */
	gettimeofday(&start, NULL);
	interp(q4_file, xo);
	gettimeofday(&stop, NULL);
	elapsed_ms = (stop.tv_sec - start.tv_sec) * 1000.0;
	elapsed_ms += (stop.tv_usec - start.tv_usec) / 1000.0;
	printf("QUESTION 4:  %.2f milliseconds\n", elapsed_ms);

	/* Question 5 */
	gettimeofday(&start, NULL);
	advection(q5_file);
	gettimeofday(&stop, NULL);
	elapsed_ms = (stop.tv_sec - start.tv_sec) * 1000.0;
	elapsed_ms += (stop.tv_usec - start.tv_usec) / 1000.0;
	printf("QUESTION 5:  %.2f milliseconds\n", elapsed_ms);

	return (EXIT_SUCCESS);
}
