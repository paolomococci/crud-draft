/**
 * 
 * Copyright 2018 paolo mococci
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package local.example.draft.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import local.example.draft.validator.constraints.Alpha;

/**
 *
 * @author paolo mococci
 */
public class AlphaValidator 
        implements ConstraintValidator<Alpha, String> {

    @Override
    public void initialize(Alpha a) {}

    @Override
    public boolean isValid(String str, ConstraintValidatorContext cvc) {
        if(str == null) {
            return true;
        }
        if (str.matches("[a-zA-Z]+")) {
            return true;
        }
        else {
            return false;
        }
    }
    
}
