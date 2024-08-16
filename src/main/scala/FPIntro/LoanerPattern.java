/*
 * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package FPIntro;

import java.io.File;

interface FileHandler<T> {
    T handler(File file);
}

public class LoanerPattern {
    public Boolean processFile(FileHandler<Boolean> handle) {
        return handle.handler(new File("cs474grades.txt"));
    }

    public static void main(String[] args) {
        new LoanerPattern().processFile(new FileHandler<Boolean>() {
            @Override
            public Boolean handler(File file) {
                try {
                    return file.isDirectory();
                } finally {
                    file.delete();
                }
            }
        });
    }
}
