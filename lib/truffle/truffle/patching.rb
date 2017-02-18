module Truffle::Patching
  TRUFFLE_PATCHES_DIRECTORY = File.join(Truffle::Boot.ruby_home, 'lib', 'patches')
  TRUFFLE_PATCHES           = Dir.glob(File.join(TRUFFLE_PATCHES_DIRECTORY, '**', '*.rb')).
      select { |path| File.file? path }.
      map { |path| path[(TRUFFLE_PATCHES_DIRECTORY.size+1)..-4] }.
      reduce ({}) { |h, v| h.update v => true }
end

module Kernel

  private

  alias_method :require_without_patching, :require

  def require(path)
    required = require_without_patching path

    if required && Truffle::Patching::TRUFFLE_PATCHES[path]
      puts "[ruby] PATCH applying #{path}"
      load File.join(Truffle::Patching::TRUFFLE_PATCHES_DIRECTORY, path + '.rb')
    end
    required
  end
end

class Module

  alias_method :autoload_without_patching, :autoload
  private :autoload_without_patching

  def autoload(const, path)
    if Truffle::Patching::TRUFFLE_PATCHES[path]
      require path
    else
      autoload_without_patching const, path
    end
  end
end
