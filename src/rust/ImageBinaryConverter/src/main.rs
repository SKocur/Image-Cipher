extern crate image;

use std::env;
use std::io::prelude::*;
use std::fs::File;
use std::iter::FromIterator;
use image::ImageBuffer;
use image::Rgb;

fn main() {
    let args: Vec<String> = env::args().collect();

    if args.len() > 1 {
        let mode = &args[1];

        if mode == "enc" {
            encrypt_vector_files(Vec::from_iter(args[2..].iter().cloned()));
        } else if mode == "dec" {
            // TODO: Implement decryption mechanism
        } else {
            panic!("Invalid mode");
        }
    }
}

fn encrypt_vector_files(files: Vec<String>) {
    for file in files {
        encrypt_file(file);
    }
}

fn encrypt_file(file_name: String) {
    let mut file = File::open(&file_name)
        .expect("Cannot read file");

    let mut buffer = Vec::new();
    file.read_to_end(&mut buffer)
        .expect("Cannot load data to buffer");

    let size_y: u32 = 500;
    let size_x: u32 = buffer.len() as u32 / size_y;
    let mut image = ImageBuffer::<Rgb<u8>, Vec<u8>>::new(size_x, size_y);

    let mut counter: usize = 0;
    for y in 0..size_y - 1 {
        for x in 0..size_x {
            if counter == buffer.len() {
                break;
            } else if counter + 1 == buffer.len() {
                image.get_pixel_mut(x, y).data = [buffer[counter], 0, 0];
                break;
            } else if counter + 2 == buffer.len() {
                image.get_pixel_mut(x, y).data = [buffer[counter], buffer[counter + 1], 0];
                break;
            } else {
                image.get_pixel_mut(x, y).data = [buffer[counter], buffer[counter + 1], buffer[counter + 2]];
            }

            counter += 3;
        }
    }

    image.save(format!("{}.png", file_name)).unwrap();
}