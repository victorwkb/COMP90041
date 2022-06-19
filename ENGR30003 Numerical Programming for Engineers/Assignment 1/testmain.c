/***************************************************************************
 *
 *   File        : main.c
 *   Student Id  : 981436
 *   Name        : KAI BING GOH
 *
 ***************************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <sys/time.h>
#include <string.h>
#include "tasks.h"
#include <assert.h>

int main(int argc, char *argv[]) {
	
	/* TODO: Parse Command Line Arguments */
	char* flow_file = NULL;
	int grid_resolution = 0;
	int vorticity_numthreshold = 0;
    
	flow_file = argv[1];
	grid_resolution = atoi(argv[2]);
    vorticity_numthreshold = atoi(argv[3]);

    // printf("%s, %d, %d\n",flow_file,grid_resolution,vorticity_numthreshold);
    
	struct timeval start;
	struct timeval stop;
    
	/* Task 1: Find the maximum/minimum flux */
	gettimeofday(&start, NULL);
	max_min_flux(flow_file);
	gettimeofday(&stop, NULL);
	double elapsed_ms = (stop.tv_sec - start.tv_sec) * 1000.0;
	elapsed_ms += (stop.tv_usec - start.tv_usec) / 1000.0;
	printf("TASK 1:  %.2f milliseconds\n", elapsed_ms);
	
	/* Task 2: Interpolating on a coarser grid */
	gettimeofday(&start, NULL);
	coarsegrid(flow_file, grid_resolution);
	gettimeofday(&stop, NULL);
    	elapsed_ms = (stop.tv_sec - start.tv_sec) * 1000.0;
    	elapsed_ms += (stop.tv_usec - start.tv_usec) / 1000.0;
    	printf("TASK 2:  %.2f milliseconds\n", elapsed_ms);
	
	/* Task 3: Searching */
	gettimeofday(&start, NULL);
	searching(flow_file);
	gettimeofday(&stop, NULL);
    	elapsed_ms = (stop.tv_sec - start.tv_sec) * 1000.0;
    	elapsed_ms += (stop.tv_usec - start.tv_usec) / 1000.0;
   	printf("TASK 3:  %.2f milliseconds\n", elapsed_ms);
    
	/* Task 4: Computing vorticity */
	gettimeofday(&start, NULL);
	calculate_vorticity(flow_file,vorticity_numthreshold);
	gettimeofday(&stop, NULL);
    	elapsed_ms = (stop.tv_sec - start.tv_sec) * 1000.0;
    	elapsed_ms += (stop.tv_usec - start.tv_usec) / 1000.0;
    	printf("TASK 4:  %.2f milliseconds\n", elapsed_ms);
	
    
	return (EXIT_SUCCESS);
}
