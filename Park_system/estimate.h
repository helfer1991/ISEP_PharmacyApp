#ifndef ESTIMATE_H
#define ESTIMATE_H

typedef struct {
	int chargerOutput; // kW
	int scooterId; //
	int scooterBatteryCapacity;// kWh
	int scooterBatteryPercentage; // p.e. 70%
	int correctlyDocked; // 1=true| 0=false
} dockingSensor; 

int estimate(dockingSensor *sensor1);
int readDataFile(char * filePath, dockingSensor *sensor1);
int createFiles(dockingSensor *sensor1, float estimatedTime, char* original_file_name, int isReestimate);
char* check_dir_changes(char * directory, char* newFileName);
#endif
