/***************************************************************************
 *
 *   File        : tasks.h
 *   Student Id  : <INSERT STUDENT ID HERE>
 *   Name        : <INSERT STUDENT NAME HERE>
 *
 ***************************************************************************/

#ifndef TASKS_H
#define TASKS_H

void max_min_flux(const char* flow_file);

void coarsegrid(const char* flow_file, int grid_resolution);

void searching(const char* flow_file);

void calculate_vorticity(const char* flow_file, int vorticity_numthreshold);

#endif
