# - shell to tar for untarring
# - skip some checksum/digest verification

class Gem::Package
  def extract_files destination_dir, pattern = "*"
    verify unless @spec

    FileUtils.mkdir_p destination_dir

    # WORKAROUND START
    extr_to = File.dirname(@gem.path) + '/' + File.basename(@gem.path, File.extname(@gem.path))
    Dir.mkdir(extr_to)
    `tar -C #{extr_to} -xf #{@gem.path}`
    # WORKAROUND END

    @gem.with_read_io do |io|
      reader = Gem::Package::TarReader.new io

      reader.each do |entry|
        next unless entry.full_name == 'data.tar.gz'

        # WORKAROUND START
        `tar -C #{destination_dir} -xzf #{extr_to + '/' + entry.full_name}`
        #extract_tar_gz entry, destination_dir, pattern
        FileUtils.remove_dir(extr_to)
        # WORKAROUND END

        return # ignore further entries
      end
    end
  end

  def verify
    @files = []
    @spec  = nil

    @gem.with_read_io do |io|
      Gem::Package::TarReader.new io do |reader|
        read_checksums reader

        verify_files reader
      end
    end

    # WORKAROUND START
    #verify_checksums @digests, @checksums
    # WORKAROUND END

    @security_policy.verify_signatures @spec, @digests, @signatures if @security_policy

    true
  rescue Gem::Security::Exception
    @spec  = nil
    @files = []
    raise
  rescue Errno::ENOENT => e
    raise Gem::Package::FormatError.new e.message
  rescue Gem::Package::TarInvalidError => e
    raise Gem::Package::FormatError.new e.message, @gem
  end

  def verify_entry entry
    file_name = entry.full_name
    @files << file_name

    case file_name
    when /\.sig$/ then
      @signatures[$`] = entry.read if @security_policy
      return
    else
      # WORKAROUND START
      #digest entry
      # WORKAROUND END
    end

    case file_name
    when /^metadata(.gz)?$/ then
      load_spec entry
    when 'data.tar.gz' then
      # WORKAROUND START
      #verify_gz entry
      # WORKAROUND END
    end
  rescue => e
    message = "package is corrupt, exception while verifying: " +
        "#{e.message} (#{e.class})"
    raise Gem::Package::FormatError.new message, @gem
  end

end
