#include <stdio.h>
#include <sys/types.h>
#include <linux/inotify.h>
#include <string.h>
#include "estimate.h"

#define EVENT_SIZE  ( sizeof (struct inotify_event) )
#define EVENT_BUF_LEN     ( 1024 * ( EVENT_SIZE + 16 ) )

char* check_dir_changes(char * directory, char* lockFlagFileName){

  int length, i = 0;
  int fd;
  int wd;
  char buffer[EVENT_BUF_LEN];

  /*creating the INOTIFY instance*/
  fd = inotify_init();

  /*checking for error*/
  if ( fd < 0 ) {
    perror( "inotify_init" );
  }
	
	/*adding the “/tmp” directory into watch list. Here, the suggestion is to validate the existence of the directory before adding into monitoring list.*/
  wd = inotify_add_watch( fd, directory, IN_CREATE | IN_MOVED_TO );

  /*read to determine the event change happens on “/tmp” directory. Actually this read blocks until the change event occurs*/ 

  length = read( fd, buffer, EVENT_BUF_LEN ); 

  /*checking for error*/
  if ( length < 0 ) {
    perror( "read" );
  }  
	
  /*actually read return the list of change events happens. Here, read the change event one by one and process it accordingly.*/
  while ( i < length ) {
	  struct inotify_event *event = (struct inotify_event *) &buffer[ i ];
	  if ( event->len )
	  {
		if ( event->mask & IN_CREATE ) {
			if ( event->mask & IN_ISDIR ) {
			printf( "Detected: New directory %s created inside the input directory.\n", event->name );
			}
			else {
				printf( "Detected: New file %s created inside the input directory.\n", event->name );
				strcpy(lockFlagFileName, event->name);
			}
		}
      if ( event->mask & IN_MOVED_TO ) {
        if ( event->mask & IN_ISDIR ) {
          printf( "Detected: New directory %s moved into the input directory.\n", event->name );
        }
        else {
          printf( "Detected: New file  %s moved into the input directory.\n", event->name );
          strcpy(lockFlagFileName, event->name);
        }
      }
    }
    i += EVENT_SIZE + event->len;
  }
	
	
	/*removing the directory from the watch list.*/
   inotify_rm_watch( fd, wd );

  /*closing the INOTIFY instance*/
   close( fd );
   
   return lockFlagFileName;
}
