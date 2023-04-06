import argparse
import base64
import json

def get_input(prompt):
    return input(prompt).strip()

def get_base64_encoded_file_contents(filename):
    with open(filename, 'rb') as f:
        return base64.b64encode(f.read()).decode('utf-8')

def create_json_request(song_file, song_title, song_artist):
    song_base64 = get_base64_encoded_file_contents(song_file)
    file_name = song_file.split('/')[-1]
    return {
        "songFile": {
            "songBase64": song_base64,
            "fileName": file_name
        },
        "songtitle": song_title,
        "songartist": song_artist
    }

def main():
    parser = argparse.ArgumentParser(description='Generate JSON requests for uploading to SongShare')
    parser.add_argument('--file', dest='song_file', required=True,
                        help='the path to the .mp3 file')
    args = parser.parse_args()

    song_title = get_input('Enter the song title: ')
    song_artist = get_input('Enter the song artist: ')

    json_request = create_json_request(args.song_file, song_title, song_artist)
    with open('request.json', 'w') as f:
        f.write(json.dumps(json_request))

if __name__ == '__main__':
    main()
