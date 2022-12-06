#include <stdio.h>
#include <stdlib.h>
int main(){
int rv;
const char *command ="adb shell sh /data/local/tmp/exploit.sh";
rv = system(command);
if(rv==0) {
   exit(0);
}
return 0;
}
