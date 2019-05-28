/*
 * This file is generated by jOOQ.
 */
package org.jooq.example.jpa.jooq.tables;


import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.example.jpa.jooq.Indexes;
import org.jooq.example.jpa.jooq.Keys;
import org.jooq.example.jpa.jooq.Public;
import org.jooq.example.jpa.jooq.tables.records.FilmActorRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FilmActor extends TableImpl<FilmActorRecord> {

    private static final long serialVersionUID = -705857387;

    /**
     * The reference instance of <code>PUBLIC.FILM_ACTOR</code>
     */
    public static final FilmActor FILM_ACTOR = new FilmActor();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<FilmActorRecord> getRecordType() {
        return FilmActorRecord.class;
    }

    /**
     * The column <code>PUBLIC.FILM_ACTOR.FILMS_FILMID</code>.
     */
    public final TableField<FilmActorRecord, Integer> FILMS_FILMID = createField(DSL.name("FILMS_FILMID"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>PUBLIC.FILM_ACTOR.ACTORS_ACTORID</code>.
     */
    public final TableField<FilmActorRecord, Integer> ACTORS_ACTORID = createField(DSL.name("ACTORS_ACTORID"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * Create a <code>PUBLIC.FILM_ACTOR</code> table reference
     */
    public FilmActor() {
        this(DSL.name("FILM_ACTOR"), null);
    }

    /**
     * Create an aliased <code>PUBLIC.FILM_ACTOR</code> table reference
     */
    public FilmActor(String alias) {
        this(DSL.name(alias), FILM_ACTOR);
    }

    /**
     * Create an aliased <code>PUBLIC.FILM_ACTOR</code> table reference
     */
    public FilmActor(Name alias) {
        this(alias, FILM_ACTOR);
    }

    private FilmActor(Name alias, Table<FilmActorRecord> aliased) {
        this(alias, aliased, null);
    }

    private FilmActor(Name alias, Table<FilmActorRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> FilmActor(Table<O> child, ForeignKey<O, FilmActorRecord> key) {
        super(child, key, FILM_ACTOR);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.FK3FSUXQ0JJ1XONRE7BHROOPVBX_INDEX_7, Indexes.FK43SD2F45W7YN0GAXQ94EHTWT2_INDEX_7, Indexes.PRIMARY_KEY_7);
    }

    @Override
    public UniqueKey<FilmActorRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_7;
    }

    @Override
    public List<UniqueKey<FilmActorRecord>> getKeys() {
        return Arrays.<UniqueKey<FilmActorRecord>>asList(Keys.CONSTRAINT_7);
    }

    @Override
    public List<ForeignKey<FilmActorRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<FilmActorRecord, ?>>asList(Keys.FK3FSUXQ0JJ1XONRE7BHROOPVBX, Keys.FK43SD2F45W7YN0GAXQ94EHTWT2);
    }

    public Film film() {
        return new Film(this, Keys.FK3FSUXQ0JJ1XONRE7BHROOPVBX);
    }

    public Actor actor() {
        return new Actor(this, Keys.FK43SD2F45W7YN0GAXQ94EHTWT2);
    }

    @Override
    public FilmActor as(String alias) {
        return new FilmActor(DSL.name(alias), this);
    }

    @Override
    public FilmActor as(Name alias) {
        return new FilmActor(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public FilmActor rename(String name) {
        return new FilmActor(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public FilmActor rename(Name name) {
        return new FilmActor(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<Integer, Integer> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}
