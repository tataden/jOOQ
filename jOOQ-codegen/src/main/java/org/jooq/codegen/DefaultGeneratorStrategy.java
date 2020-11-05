/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Other licenses:
 * -----------------------------------------------------------------------------
 * Commercial licenses for this work are available. These replace the above
 * ASL 2.0 and offer limited warranties, support, maintenance, and commercial
 * database integrations.
 *
 * For more information, please visit: http://www.jooq.org/licenses
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package org.jooq.codegen;

import static java.util.Arrays.asList;
// ...
// ...
// ...
// ...
import static org.jooq.SQLDialect.MARIADB;
// ...
import static org.jooq.SQLDialect.MYSQL;
import static org.jooq.SQLDialect.POSTGRES;
// ...
// ...
import static org.jooq.codegen.AbstractGenerator.Language.KOTLIN;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// ...
import org.jooq.codegen.AbstractGenerator.Language;
import org.jooq.meta.ArrayDefinition;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.ColumnDefinition;
import org.jooq.meta.ConstraintDefinition;
import org.jooq.meta.Definition;
import org.jooq.meta.DomainDefinition;
import org.jooq.meta.EmbeddableDefinition;
import org.jooq.meta.EnumDefinition;
import org.jooq.meta.ForeignKeyDefinition;
import org.jooq.meta.IdentityDefinition;
import org.jooq.meta.IndexDefinition;
import org.jooq.meta.PackageDefinition;
import org.jooq.meta.RoutineDefinition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.meta.SequenceDefinition;
import org.jooq.meta.TableDefinition;
import org.jooq.meta.UDTDefinition;
import org.jooq.meta.UniqueKeyDefinition;
// ...
// ...
import org.jooq.tools.StringUtils;

/**
 * The default naming strategy for the {@link JavaGenerator}
 *
 * @author Lukas Eder
 */
public class DefaultGeneratorStrategy extends AbstractGeneratorStrategy {

    private String   targetDirectory;
    private String   targetPackage;
    private Locale   targetLocale               = Locale.getDefault();
    private Language targetLanguage             = Language.JAVA;
    private boolean  instanceFields             = true;
    private boolean  javaBeansGettersAndSetters = false;

    // -------------------------------------------------------------------------
    // Initialisation
    // -------------------------------------------------------------------------

    @Override
    public void setInstanceFields(boolean instanceFields) {
        this.instanceFields = instanceFields;
    }

    @Override
    public boolean getInstanceFields() {
        return instanceFields;
    }

    @Override
    public void setJavaBeansGettersAndSetters(boolean javaBeansGettersAndSetters) {
        this.javaBeansGettersAndSetters = javaBeansGettersAndSetters;
    }

    @Override
    public boolean getJavaBeansGettersAndSetters() {
        return javaBeansGettersAndSetters;
    }

    @Override
    public String getTargetDirectory() {
        return targetDirectory;
    }

    @Override
    public void setTargetDirectory(String directory) {
        this.targetDirectory = directory;
    }

    @Override
    public String getTargetPackage() {
        return targetPackage;
    }

    @Override
    public void setTargetPackage(String packageName) {
        this.targetPackage = packageName;
    }

    @Override
    public Locale getTargetLocale() {
        return targetLocale;
    }

    @Override
    public void setTargetLocale(Locale targetLocale) {
        this.targetLocale = targetLocale;
    }

    @Override
    public Language getTargetLanguage() {
        return targetLanguage;
    }

    @Override
    public void setTargetLanguage(Language targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    // -------------------------------------------------------------------------
    // Strategy methods
    // -------------------------------------------------------------------------

    @Override
    public String getGlobalReferencesFileHeader(Definition container, Class<? extends Definition> objectType) {
        return "This file is generated by jOOQ.";
    }

    @Override
    public String getFileHeader(Definition definition, Mode mode) {
        return "This file is generated by jOOQ.";
    }

    @Override
    public String getJavaIdentifier(Definition definition) {
        String identifier = getFixedJavaIdentifier(definition);

        if (identifier != null)
            return identifier;

        // [#6307] Some databases work with per-table namespacing for indexes, not per-schema namespacing.
        //         In order to have non-ambiguous identifiers, we need to include the table name.
        else if (definition instanceof IndexDefinition && asList(MARIADB, MYSQL).contains(definition.getDatabase().getDialect().family()))
            return ((IndexDefinition) definition).getTable().getOutputName().toUpperCase(targetLocale) + "_" + definition.getOutputName().toUpperCase(targetLocale);







        // [#9758] And then also for foreign keys
        else if (definition instanceof ForeignKeyDefinition && asList(POSTGRES).contains(definition.getDatabase().getDialect().family()))
            return ((ForeignKeyDefinition) definition).getTable().getOutputName().toUpperCase(targetLocale) + "__" + definition.getOutputName().toUpperCase(targetLocale);

        // [#10481] Embeddables have a defining name (class name) and a referencing name (identifier name, member name).
        else if (definition instanceof EmbeddableDefinition)
            return ((EmbeddableDefinition) definition).getReferencingOutputName().toUpperCase(targetLocale);






        else
            return definition.getOutputName().toUpperCase(targetLocale);
    }
















    private String getterSetterSuffix(Definition definition) {

        // [#5354] Please forgive me but this is how it works.
        if (javaBeansGettersAndSetters) {
            String name = getJavaMemberName(definition);

            if (Character.isUpperCase(name.charAt(0)))
                return name;
            if (name.length() > 1 && Character.isUpperCase(name.charAt(1)))
                return name;

            char chars[] = name.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        }

        // [#10481] Embeddables have a defining name (class name) and a referencing name (identifier name, member name).
        if (definition instanceof EmbeddableDefinition)
            return getJavaClassName0(((EmbeddableDefinition) definition).getReferencingOutputName(), Mode.DEFAULT);
        else
            return getJavaClassName0(definition, Mode.DEFAULT);
    }

    @Override
    public String getJavaSetterName(Definition definition, Mode mode) {
        return "set" + getterSetterSuffix(definition);
    }

    @Override
    public String getJavaGetterName(Definition definition, Mode mode) {
        return "get" + getterSetterSuffix(definition);
    }

    @Override
    public String getJavaMethodName(Definition definition, Mode mode) {
        // [#7148] If table A references table B only once, then B is the ideal name
        //         for the implicit JOIN path. Otherwise, fall back to the foreign key name
        if (definition instanceof ForeignKeyDefinition) {
            ForeignKeyDefinition fk = (ForeignKeyDefinition) definition;
            TableDefinition referenced = fk.getReferencedTable();

            if (fk.getKeyTable().getForeignKeys(referenced).size() == 1)
                return getJavaMethodName(referenced, mode);
        }

        return getJavaClassName0LC(definition, Mode.DEFAULT);
    }

    @Override
    public String getGlobalReferencesJavaClassExtends(Definition container, Class<? extends Definition> objectType) {
        return null;
    }

    @Override
    public String getJavaClassExtends(Definition definition, Mode mode) {
        return null;
    }

    @Override
    public List<String> getGlobalReferencesJavaClassImplements(Definition container, Class<? extends Definition> objectType) {
        return new ArrayList<>();
    }

    @Override
    public List<String> getJavaClassImplements(Definition definition, Mode mode) {
        return new ArrayList<>();
    }

    @Override
    public String getGlobalReferencesJavaClassName(Definition container, Class<? extends Definition> objectType) {
        if (ArrayDefinition.class.isAssignableFrom(objectType))
            return "Arrays";
        else if (ConstraintDefinition.class.isAssignableFrom(objectType))
            return "Keys";
        else if (DomainDefinition.class.isAssignableFrom(objectType))
            return "Domains";
        else if (EmbeddableDefinition.class.isAssignableFrom(objectType))
            return "Embeddables";
        else if (EnumDefinition.class.isAssignableFrom(objectType))
            return "Enums";
        else if (IdentityDefinition.class.isAssignableFrom(objectType))
            return "Identities";
        else if (IndexDefinition.class.isAssignableFrom(objectType))
            return "Indexes";




        else if (PackageDefinition.class.isAssignableFrom(objectType))
            return "UDTs";




        else if (RoutineDefinition.class.isAssignableFrom(objectType))
            return "Routines";
        else if (SequenceDefinition.class.isAssignableFrom(objectType))
            return "Sequences";
        else if (TableDefinition.class.isAssignableFrom(objectType))
            return "Tables";
        else if (UDTDefinition.class.isAssignableFrom(objectType))
            return "UDTs";
        else
            return "UnknownTypes";
    }

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        String name = getFixedJavaClassName(definition);

        if (name != null)
            return name;
        else
            return getJavaClassName0(definition, mode);
    }

    @Override
    public String getGlobalReferencesJavaPackageName(Definition container, Class<? extends Definition> objectType) {
        String packageName = getJavaPackageName(container);

        if (container instanceof PackageDefinition)
            packageName = packageName + "." + getJavaIdentifier(container).toLowerCase(targetLocale);

        if (targetLanguage == KOTLIN) {
            String className = getGlobalReferencesJavaClassName(container, objectType).toLowerCase(targetLocale);

            // Repeat the package name for global references to prevent collisions
            if (TableDefinition.class.isAssignableFrom(objectType))
                packageName = packageName + "." + className + ".references";
            else if (RoutineDefinition.class.isAssignableFrom(objectType))
                packageName = packageName + "." + className + ".references";
            else if (UDTDefinition.class.isAssignableFrom(objectType))
                packageName = packageName + "." + className + ".references";
            else
                packageName = packageName + "." + className;
        }

        return packageName;
    }

    @Override
    public String getJavaPackageName(Definition definition, Mode mode) {
        StringBuilder sb = new StringBuilder();

        sb.append(getTargetPackage());

        // [#2032] In multi-catalog setups, the catalog name goes into the package
        if (definition.getDatabase().getCatalogs().size() > 1) {
            sb.append(".");
            sb.append(getJavaIdentifier(definition.getCatalog()).toLowerCase(targetLocale));
        }

        if (!(definition instanceof CatalogDefinition)) {

            // [#282] In multi-schema setups, the schema name goes into the package
            if (definition.getDatabase().getSchemata().size() > 1) {
                sb.append(".");
                sb.append(getJavaIdentifier(definition.getSchema()).toLowerCase(targetLocale));
            }

            if (!(definition instanceof SchemaDefinition)) {

                // Some definitions have their dedicated subpackages, e.g. "tables", "routines"
                String subPackage = getSubPackage(definition);
                if (!StringUtils.isBlank(subPackage)) {
                    sb.append(".");
                    sb.append(subPackage);
                }

                // Record are yet in another subpackage
                if (mode == Mode.RECORD) {
                    sb.append(".records");
                }

                // POJOs too
                else if (mode == Mode.POJO) {
                    sb.append(".pojos");
                }

                // DAOs too
                else if (mode == Mode.DAO) {
                    sb.append(".daos");
                }

                // Interfaces too
                else if (mode == Mode.INTERFACE) {
                    sb.append(".interfaces");
                }






            }
        }

        return sb.toString();
    }

    @Override
    public String getJavaMemberName(Definition definition, Mode mode) {

        // [#10481] Embeddables have a defining name (class name) and a referencing name (identifier name, member name).
        if (definition instanceof EmbeddableDefinition)
            return getJavaClassName0LC(((EmbeddableDefinition) definition).getReferencingOutputName(), mode);
        else
            return getJavaClassName0LC(definition, mode);
    }

    private String getJavaClassName0LC(Definition definition, Mode mode) {
        String result = getJavaClassName0(definition, mode);
        return result.substring(0, 1).toLowerCase(targetLocale) + result.substring(1);
    }

    private String getJavaClassName0LC(String outputName, Mode mode) {
        String result = getJavaClassName0(outputName, mode);
        return result.substring(0, 1).toLowerCase(targetLocale) + result.substring(1);
    }

    private String getJavaClassName0(Definition definition, Mode mode) {
        return getJavaClassName0(definition.getOutputName(), mode);
    }

    private String getJavaClassName0(String outputName, Mode mode) {
        StringBuilder result = new StringBuilder();

        // [#4562] Some characters should be treated like underscore
        result.append(StringUtils.toCamelCase(
            outputName.replace(' ', '_')
                      .replace('-', '_')
                      .replace('.', '_')
        ));

        if (mode == Mode.RECORD)
            result.append("Record");
        else if (mode == Mode.DAO)
            result.append("Dao");
        else if (mode == Mode.INTERFACE)
            result.insert(0, "I");

        return result.toString();
    }

    private String getSubPackage(Definition definition) {
        if (definition instanceof TableDefinition) {
            return "tables";
        }

        // [#2530] Embeddable types
        else if (definition instanceof EmbeddableDefinition) {
            return "embeddables";
        }

        // [#799] UDT's are also packages
        else if (definition instanceof UDTDefinition) {
            UDTDefinition udt = (UDTDefinition) definition;

            // [#330] [#6529] A UDT inside of a package is a PL/SQL RECORD type
            if (udt.getPackage() != null)
                return "packages." + getJavaIdentifier(udt.getPackage()).toLowerCase(targetLocale) + ".udt";
            else
                return "udt";
        }
        else if (definition instanceof PackageDefinition) {
            return "packages";
        }
        else if (definition instanceof RoutineDefinition) {
            RoutineDefinition routine = (RoutineDefinition) definition;

            if (routine.getPackage() instanceof UDTDefinition) {
                return "udt." + getJavaIdentifier(routine.getPackage()).toLowerCase(targetLocale);
            }
            else if (routine.getPackage() != null) {
                return "packages." + getJavaIdentifier(routine.getPackage()).toLowerCase(targetLocale);
            }
            else {
                return "routines";
            }
        }
        else if (definition instanceof EnumDefinition) {
            return "enums";
        }
        else if (definition instanceof DomainDefinition) {
            return "domains";
        }

        else if (definition instanceof ArrayDefinition) {
            ArrayDefinition array = (ArrayDefinition) definition;

            // [#7125] An array inside of a package is a PL/SQL TABLE type
            if (array.getPackage() != null)
                return "packages." + getJavaIdentifier(array.getPackage()).toLowerCase(targetLocale) + ".udt";
            else
                return "udt";
        }

        // Default always to the main package
        return "";
    }

    @Override
    public String getOverloadSuffix(Definition definition, Mode mode, String overloadIndex) {
        return overloadIndex;
    }
}
