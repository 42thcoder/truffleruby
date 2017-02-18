old_value = Bundler::Dependency::PLATFORM_MAP
# add record for truffleruby
Bundler::Dependency.send :const_set,
                         :PLATFORM_MAP,
                         old_value.merge(truffleruby: Gem::Platform::RUBY)

Bundler::Dependency.send(:const_set, :REVERSE_PLATFORM_MAP, {}.tap do |reverse_platform_map|
  Bundler::Dependency::PLATFORM_MAP.each do |key, value|
    reverse_platform_map[value] ||= []
    reverse_platform_map[value] << key
  end

  reverse_platform_map.each { |_, platforms| platforms.freeze }
end.freeze)
