DO $$
BEGIN

  IF EXISTS (
      SELECT 1
      FROM information_schema.columns
      WHERE table_schema = 'chat'
        AND table_name = 'contact'
        AND column_name = 'is_active'
  ) THEN
    ALTER TABLE contact DROP COLUMN is_active;
  END IF;
END $$;