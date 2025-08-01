
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![GitHub version](https://badge.fury.io/gh/boennemann%2Fbadges.svg)](https://github.com/SKocur/Image-Cipher)

# Image-Cipher
Steganography software for encrypting text into image that uses modified version of LSB (Least Significant Bit) algorithm. Project is part of this [Medium article](https://medium.com/@szymonkocur/how-i-redesigned-lsb-steganography-algorithm-ee45503dd47).

## Usage:

### GUI Application
Launch desktop application:
```bash
./gradlew :app:run
```
Or run the compiled JAR:
```bash
java -jar app.jar
```

### Command Line Interface

The CLI has been redesigned with intuitive parameters and comprehensive help:

```bash
# Show help
java -jar app.jar --help

# Show version
java -jar app.jar --version
```

#### Encryption Examples:
```bash
# Encrypt text into image (interactive input)
java -jar app.jar --encrypt single-color --input photo.jpg

# Encrypt specific text
java -jar app.jar -e multi-color -i image.png -t "Secret message"

# Encrypt with output file
java -jar app.jar -e low-level-bit -i input.jpg -t "Hidden data" -o encrypted.png

# Encrypt from stdin
java -jar app.jar -e single-color -i photo.jpg -t :stdin
```

#### Decryption Examples:
```bash
# Decrypt text from image
java -jar app.jar --decrypt single-color --input encrypted.jpg

# Decrypt with verbose output
java -jar app.jar -d multi-color -i hidden.png --verbose

# Decrypt low-level bit data
java -jar app.jar -d low-level-bit -i steganographic.png
```

#### Available Algorithms:
- **single-color**: Basic LSB steganography in single color channel
- **multi-color**: Enhanced LSB using multiple color channels  
- **low-level-bit**: Advanced bit-level manipulation
- **rsa**: RSA encryption with steganography (experimental, encryption only)

#### CLI Parameters:
- `-h, --help` - Show help message with examples
- `-e, --encrypt <algorithm>` - Encrypt text using specified algorithm
- `-d, --decrypt <algorithm>` - Decrypt text using specified algorithm
- `-i, --input, --image <file>` - Input image file path
- `-o, --output <file>` - Output image file (encryption only)
- `-t, --text <text>` - Text to encrypt (use `:stdin` for interactive input)
- `-c, --certificate <file>` - Certificate file for RSA encryption
- `-v, --verbose` - Enable detailed output
- `--version` - Show version information

### Before encryption
![Demo](images/github-logo.jpeg)

### After encryption
![Demo](images/output.jpeg)

### How does it work?
![Demo](images/encryption_description.png)

### Error codes:
1 - IOException

2 - Invalid arguments (for example encrypting and decrypting at the same time)

### Documentation is available on: [JavaDoc](https://skocur.github.io/Image-Cipher/)

## Additional tools
#### [BinaryToImageConverter](https://gist.github.com/SKocur/edd29a369e6097dbc5d7bef9c35a116e) - converts any binary file to image.


[![Star History Chart](https://api.star-history.com/svg?repos=SKocur/Image-Cipher&type=Date)](https://www.star-history.com/#SKocur/Image-Cipher&Date)